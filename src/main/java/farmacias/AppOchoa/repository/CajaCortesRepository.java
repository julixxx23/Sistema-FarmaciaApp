package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.CajaCorte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CajaCortesRepository extends JpaRepository<CajaCorte, Long> {

    // Buscar cortes de una sesión específica
    List<CajaCorte> findByCajaSesionesId(Long sesionId);

    // Buscar cortes autorizados por un supervisor
    List<CajaCorte> findByUsuarioId(Long supervisorId);

    // Buscar cortes por rango de fechas
    List<CajaCorte> findByCorteFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}