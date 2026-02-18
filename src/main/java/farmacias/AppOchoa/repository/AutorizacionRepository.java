package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Autorizacion;
import farmacias.AppOchoa.model.AutorizacionTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AutorizacionRepository extends JpaRepository<Autorizacion, Long> {
    List<Autorizacion> findBySupervisorId(Long supervisorId);
    List<Autorizacion> findByAutorizacionFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Autorizacion> findByAutorizacionTipo(AutorizacionTipo autorizacionTipo);
}
