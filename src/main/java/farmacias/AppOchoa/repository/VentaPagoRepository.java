package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.MetodoPagoEstado;
import farmacias.AppOchoa.model.VentaPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaPagoRepository extends JpaRepository<VentaPago, Long> {
    List<VentaPago> findByMetodoPago(MetodoPagoEstado metodoPagoEstado);
    List<VentaPago> findByCajaSesionesId(Long sesionId);
    List<VentaPago> findByVentaId(Long ventaId);

}
