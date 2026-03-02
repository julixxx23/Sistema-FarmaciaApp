package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.CajaCorte;
import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.SesionEstado;
import farmacias.AppOchoa.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica si ya existe un usuario con ese nombre de usuario
    boolean existsByNombreUsuarioUsuario(String nombreUsuarioUsuario);
    // Busca un usuario por su nombre de usuario
    Optional<Usuario> findByNombreUsuarioUsuario(String nombreUsuarioUsuario);
    // Lista todos los usuarios que tengan usuarioEstado = true - con paginación
    Page<Usuario> findByUsuarioEstadoTrue(Pageable pageable);
    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nombreUsuarioUsuario) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(u.usuarioNombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(u.usuarioApellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(CAST(u.usuarioRol AS string)) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(u.sucursal.sucursalNombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Usuario> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}
