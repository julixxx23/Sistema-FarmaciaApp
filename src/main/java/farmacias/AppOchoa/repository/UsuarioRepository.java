package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica si ya existe un usuario con ese nombre de usuario
    boolean existsByNombreUsuarioUsuario(String nombreUsuarioUsuario);

    // Busca un usuario por su nombre de usuario (para login, por ejemplo)
    Optional<Usuario> findByNombreUsuarioUsuario(String nombreUsuarioUsuario);

    // Lista todos los usuarios que tengan usuarioEstado = true
    List<Usuario> findByUsuarioEstadoTrue();
}
