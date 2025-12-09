package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {

    //Para validar si existe antes de crear
    boolean existsByNombrePresentacion(String nombre);

    //Para listar presentaciones activas
    List<Presentacion> findByPresentacionEstadoTrue();
}
