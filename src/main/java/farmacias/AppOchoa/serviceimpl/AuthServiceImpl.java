package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.config.JwtConfig;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.services.AuthService;
import farmacias.AppOchoa.util.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    public AuthServiceImpl(
            @Lazy AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Map<String, Object> login(String nombreUsuario, String contrasena) {

        // Spring llama a loadUserByUsername + BCrypt + isEnabled internamente
        // Si algo falla lanza BadCredentialsException o DisabledException solo
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nombreUsuario, contrasena)
        );

        // Cast directo porque Usuario ahora implementa UserDetails
        Usuario usuario = (Usuario) auth.getPrincipal();

        //Claims personalizados (Consumirlos en JwtUtil)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getUsuarioId());
        claims.put("rol", usuario.getUsuarioRol().name());
        claims.put("nombre", usuario.getUsuarioNombre());
        claims.put("apellido", usuario.getUsuarioApellido());
        claims.put("farmaciaId", usuario.getFarmacia().getFarmaciaId());

        String token = jwtUtil.generateToken(claims, nombreUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("tipo", "Bearer");
        response.put("usuarioId", usuario.getUsuarioId());
        response.put("farmaciaId", usuario.getFarmacia().getFarmaciaId());
        response.put("nombreUsuario", usuario.getNombreUsuarioUsuario());
        response.put("nombreCompleto", usuario.getUsuarioNombre() + " " + usuario.getUsuarioApellido());
        response.put("rol", usuario.getUsuarioRol().name());
        response.put("expiresIn", jwtConfig.getExpiration());

        return response;
    }
}