package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
