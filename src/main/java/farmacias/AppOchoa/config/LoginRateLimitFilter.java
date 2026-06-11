package farmacias.AppOchoa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginRateLimitFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private static final String LOGIN_PATH    = "/api/v1/auth/login";
    private static final int    MAX_INTENTOS  = 10;           // máximo por ventana
    private static final long   VENTANA_MS    = 60_000L;      // 60 segundos en ms

    // Solo se confía en X-Forwarded-For si la conexión directa viene de uno de
    // estos hosts. El proxy inverso (Nginx / LB) corre en el mismo host que la
    // app en el despliegue Docker, así que llega por loopback. Si la conexión
    // no viene de aquí, el header lo controla el cliente y es spoofeable.
    private static final Set<String> PROXIES_CONFIABLES = Set.of(
            "127.0.0.1",
            "0:0:0:0:0:0:0:1",
            "::1");

    // IP → timestamps de intentos dentro de la ventana actual
    private final ConcurrentHashMap<String, Deque<Long>> intentosPorIp = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Solo aplicar al endpoint de login
        if (!LOGIN_PATH.equals(request.getRequestURI()) || !"POST".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = obtenerIp(request);
        long ahora = System.currentTimeMillis();

        // Obtener o crear cola de timestamps para esta IP
        Deque<Long> timestamps = intentosPorIp.computeIfAbsent(ip, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            // Eliminar timestamps fuera de la ventana de tiempo
            while (!timestamps.isEmpty() && ahora - timestamps.peekFirst() > VENTANA_MS) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= MAX_INTENTOS) {
                long segundosRestantes = (VENTANA_MS - (ahora - timestamps.peekFirst())) / 1000;
                log.warn("[RateLimit] IP {} bloqueada por exceso de intentos de login. Espera: {}s", ip, segundosRestantes);
                escribirErrorJson(response, segundosRestantes);
                return;
            }

            timestamps.addLast(ahora);
        }

        filterChain.doFilter(request, response);
    }

    // Evicción periódica: sin esto el mapa acumula una entrada por cada IP
    // que alguna vez intentó login y crece indefinidamente
    @Scheduled(fixedDelay = 300_000)   // cada 5 minutos
    public void limpiarIpsInactivas() {
        long ahora = System.currentTimeMillis();
        intentosPorIp.entrySet().removeIf(entry -> {
            Deque<Long> timestamps = entry.getValue();
            synchronized (timestamps) {
                return timestamps.isEmpty() || ahora - timestamps.peekLast() > VENTANA_MS;
            }
        });
    }

    private String obtenerIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();

        // Solo se respeta X-Forwarded-For si la conexión directa viene de un proxy
        // de confianza. De lo contrario el cliente podría enviar una IP distinta en
        // cada request y evadir el límite por completo (cada intento contaría como
        // una "IP" nueva). En ese caso se usa la dirección real de la conexión.
        if (PROXIES_CONFIABLES.contains(remoteAddr)) {
            String xff = request.getHeader("X-Forwarded-For");
            if (xff != null && !xff.isBlank()) {
                // El proxy de confianza appendea la IP real al final de la lista;
                // las entradas previas pueden ser falsificadas por el cliente.
                String[] partes = xff.split(",");
                return partes[partes.length - 1].trim();
            }
        }
        return remoteAddr;
    }

    private void escribirErrorJson(HttpServletResponse response, long segundosRestantes)
            throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("estado", HttpStatus.TOO_MANY_REQUESTS.value());
        body.put("mensaje", String.format(
                "Demasiados intentos de login. Intenta nuevamente en %d segundos.", segundosRestantes));

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());  // 429
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}