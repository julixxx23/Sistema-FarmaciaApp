package farmacias.AppOchoa.config;

import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.model.UsuarioRol;
import farmacias.AppOchoa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtConfig jwtConfig
    ) {
        return args -> {

            //Validar configuración JWT (lanza excepción si está mal configurada)
            jwtConfig.validateConfig();

            //Crear usuario admin si no existe
            if (!usuarioRepository.existsByNombreUsuarioUsuario("admin")) {

                Usuario admin = Usuario.builder()
                        .nombreUsuarioUsuario("admin")
                        .usuarioContrasenaHash(passwordEncoder.encode("admin123"))
                        .usuarioNombre("Administrador")
                        .usuarioApellido("Sistema")
                        .usuarioRol(UsuarioRol.administrador)
                        .usuarioEstado(true)
                        .build();

                usuarioRepository.save(admin);
            }
        };
    }
}