package farmacias.AppOchoa.configTest;

import farmacias.AppOchoa.config.LoginRateLimitFilter;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("LoginRateLimitFilter (M1)")
public class LoginRateLimitFilterTest {

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final int MAX_INTENTOS = 10;

    private LoginRateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new LoginRateLimitFilter();
    }

    private MockHttpServletRequest loginRequest(String remoteAddr, String xff) {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", LOGIN_PATH);
        req.setRemoteAddr(remoteAddr);
        if (xff != null) {
            req.addHeader("X-Forwarded-For", xff);
        }
        return req;
    }

    // Ejecuta el filtro una vez y devuelve el status resultante.
    // Una cadena nueva por request simula peticiones independientes.
    private int ejecutar(MockHttpServletRequest req) throws ServletException, IOException {
        MockHttpServletResponse res = new MockHttpServletResponse();
        filter.doFilter(req, res, new MockFilterChain());
        return res.getStatus();
    }

    @Test
    @DisplayName("XFF spoofeado desde IP no confiable no multiplica el cupo: se bloquea al 11º intento")
    void xffSpoofeadoNoEvadeElLimite() throws Exception {
        // remoteAddr real fijo (atacante), pero XFF distinto en cada request
        for (int i = 0; i < MAX_INTENTOS; i++) {
            int status = ejecutar(loginRequest("203.0.113.7", "10.0.0." + i));
            assertEquals(HttpStatus.OK.value(), status, "intento " + (i + 1) + " debería pasar");
        }
        // El 11º (mismo remoteAddr, otro XFF falso) debe bloquearse
        int status = ejecutar(loginRequest("203.0.113.7", "10.0.0.99"));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), status);
    }

    @Test
    @DisplayName("Desde loopback se respeta XFF: IPs reales distintas tienen cupos independientes")
    void desdeLoopbackSeRespetaXff() throws Exception {
        // Agotar el cupo de un cliente real (XFF) detrás del proxy loopback
        for (int i = 0; i < MAX_INTENTOS; i++) {
            assertEquals(HttpStatus.OK.value(), ejecutar(loginRequest("127.0.0.1", "198.51.100.1")));
        }
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(),
                ejecutar(loginRequest("127.0.0.1", "198.51.100.1")));

        // Otro cliente real (otro XFF) por el mismo proxy aún tiene su cupo
        assertEquals(HttpStatus.OK.value(), ejecutar(loginRequest("127.0.0.1", "198.51.100.2")));
    }

    @Test
    @DisplayName("Desde loopback con XFF multivaluado se toma la última IP (la que añade el proxy)")
    void tomaUltimaIpDelXff() throws Exception {
        // Cliente prepende IPs falsas; el proxy añade la real (198.51.100.5) al final.
        // Agotamos su cupo y verificamos que cambiar solo las falsas no abre cupo nuevo.
        for (int i = 0; i < MAX_INTENTOS; i++) {
            assertEquals(HttpStatus.OK.value(),
                    ejecutar(loginRequest("127.0.0.1", "1.1.1." + i + ", 198.51.100.5")));
        }
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(),
                ejecutar(loginRequest("127.0.0.1", "9.9.9.9, 198.51.100.5")));
    }

    @Test
    @DisplayName("Sin XFF se usa remoteAddr")
    void sinXffUsaRemoteAddr() throws Exception {
        for (int i = 0; i < MAX_INTENTOS; i++) {
            assertEquals(HttpStatus.OK.value(), ejecutar(loginRequest("203.0.113.50", null)));
        }
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(),
                ejecutar(loginRequest("203.0.113.50", null)));
    }

    @Test
    @DisplayName("Peticiones que no son POST /login no se limitan")
    void otrosEndpointsNoSeLimitan() throws Exception {
        for (int i = 0; i < MAX_INTENTOS + 5; i++) {
            MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/v1/productos");
            req.setRemoteAddr("203.0.113.7");
            MockHttpServletResponse res = new MockHttpServletResponse();
            filter.doFilter(req, res, new MockFilterChain());
            assertEquals(HttpStatus.OK.value(), res.getStatus());
        }
    }
}
