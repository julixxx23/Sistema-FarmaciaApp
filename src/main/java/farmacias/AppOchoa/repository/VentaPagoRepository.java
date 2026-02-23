package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.MetodoPagoEstado;
import farmacias.AppOchoa.model.VentaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VentaPagoRepository extends JpaRepository<VentaPago, Long> {

    List<VentaPago> findByMetodoPago(MetodoPagoEstado metodoPagoEstado);
    List<VentaPago> findByCajaSesionesId(Long sesionId);
    List<VentaPago> findByVentaId(Long ventaId);

    // Suma el total neto (recibido - vuelto) por sesión y por método de pago (Ejemplo Tarjeta)
    @Query("SELECT COALESCE(SUM(vp.montoRecibido - vp.montoVuelto), 0) FROM VentaPago vp " +
            "WHERE vp.cajaSesiones.sesionId = :sesionId " +
            "AND vp.metodoPago = :metodoPago")
    BigDecimal sumarPorSesionYMetodo(@Param("sesionId") Long sesionId, @Param("metodoPago") MetodoPagoEstado metodoPago);

    // Suma TODO lo vendido en esa sesión sin importar cómo pagaron
    @Query("SELECT COALESCE(SUM(vp.montoRecibido - vp.montoVuelto), 0) FROM VentaPago vp " +
            "WHERE vp.cajaSesiones.sesionId= :sesionId")
    BigDecimal sumarTotalPorSesion(@Param("sesionId") Long sesionId);

}
