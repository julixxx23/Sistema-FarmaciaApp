package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.config.JwtConfig;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.AuthService;
import farmacias.AppOchoa.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public Map<String, Object> login(String nombreUsuario, String contrasena) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuarioUsuario(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getUsuarioEstado()) {
            throw new RuntimeException("Usuario desactivado");
        }

        if (!passwordEncoder.matches(contrasena, usuario.getUsuarioContrasenaHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

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