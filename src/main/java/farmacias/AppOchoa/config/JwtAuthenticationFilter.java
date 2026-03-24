package farmacias.AppOchoa.config;

import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    //Lógica JWT — generación, extracción y validación de tokens
    @Autowired
    private JwtUtil jwtUtil;

    // Carga el usuario desde DB — implementado en UsuarioServiceImpl
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Sin token — puede ser endpoint público, pasa al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Elimina el prefijo "Bearer " y extrae el token limpio
        final String jwt = authHeader.substring(7);
        final String username;

        // Token expirado, malformado o firma inválida — pasa sin autenticar
        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            log.warn("Token inválido en {}: {}", request.getRequestURI(), e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Solo autentica si el contexto está vacío — evita procesar dos veces
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Trae el usuario completo desde DB con roles y estado actualizados
            Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(username);

            // Verifica que el token pertenezca al usuario y no haya expirado
            if (jwtUtil.validateToken(jwt, usuario.getUsername())) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,                    // credentials null — token ya validado
                        usuario.getAuthorities() // ROLE_administrador, ROLE_vendedor...
                );

                // Agrega IP y datos del request al contexto de autenticación
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // A partir de acá Spring sabe quién es el usuario en este request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}