package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.VentaFel;
import farmacias.AppOchoa.model.VentaFelEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaFelRepository extends JpaRepository<VentaFel, Long> {
    List<VentaFel> findByFelEstado(VentaFelEstado ventaFelEstado);
    List<VentaFel> findByAuditoriaFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<VentaFel> findByFelEstadoAndFelIntentosLessThan(VentaFelEstado estado, Integer maxIntentos);


}
