package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica si ya existe un usuario con ese nombre de usuario
    boolean existsByNombreUsuarioUsuario(String nombreUsuarioUsuario);
    // Busca un usuario por su nombre de usuario
    Optional<Usuario> findByNombreUsuarioUsuario(String nombreUsuarioUsuario);
    // Lista todos los usuarios que tengan usuarioEstado = true - con paginación
    Page<Usuario> findByUsuarioEstadoTrue(Pageable pageable);
    Page<Usuario> findByFarmacia_FarmaciaIdAndUsuarioEstadoTrue(Long farmaciaId, Pageable pageable);
    boolean existsByFarmacia_FarmaciaIdAndNombreUsuarioUsuario(Long farmaciaId, String nombreUsuario);
    Page<Usuario> findByFarmacia_FarmaciaId(Long farmaciaId, Pageable pageable);
    Page<Usuario> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}