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
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!usuarioRepository.existsByNombreUsuarioUsuario("admin")) {

                //Crear usuario administrador por defecto
                Usuario admin = Usuario.builder()
                        .nombreUsuarioUsuario("admin")
                        .usuarioContrasenaHash(passwordEncoder.encode("admin123"))
                        .usuarioNombre("Administrador")
                        .usuarioApellido("Sistema")
                        .usuarioRol(UsuarioRol.administrador)
                        .usuarioEstado(true)
                        .build();

                usuarioRepository.save(admin);

                System.out.println();
                System.out.println("Usuario administrador creado");
                System.out.println("Username: admin");
                System.out.println("Password: admin123");
                System.out.println("Rol: ADMIN");
                System.out.println();
            } else {
                System.out.println("Usuario administrador ya existe");
            }
        };
    }
}