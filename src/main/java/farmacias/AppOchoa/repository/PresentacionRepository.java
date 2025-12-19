package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Presentacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {

    // Para validar si existe antes de crear
    boolean existsByPresentacionNombre(String nombre);

    // Para listar presentaciones activas (sin paginación)
    List<Presentacion> findByPresentacionEstadoTrue();

    // Para listar presentaciones activas CON paginación
    Page<Presentacion> findByPresentacionEstadoTrue(Pageable pageable);
}