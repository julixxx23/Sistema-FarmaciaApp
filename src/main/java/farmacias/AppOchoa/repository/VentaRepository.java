package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Venta;
import farmacias.AppOchoa.model.VentaEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    //Para reimprimir factura
    Optional<Venta> findByVentaNumeroFactura(String numeroFactura);

    //Para cierre de caja del día
    List<Venta> findBySucursalIdAndVentaFechaBetween(
            Long sucursalId, LocalDateTime inicio, LocalDateTime fin);

    List<Venta> findByVentaEstado(VentaEstado estado);

    //Para validar que no se repita número factura
    boolean existsByVentaNumeroFactura(String numeroFactura);

    //Para historial de cliente
    List<Venta> findByVentaNitCliente(String nitCliente);

    //Ventas por cajero específico (para rendición de cuentas)
    List<Venta> findByUsuarioId(Long usuarioId);

    //Ventas por cajero en turno específico
    List<Venta> findByUsuarioIdAndVentaFechaBetween(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fin);

    //Total vendido por cajero hoy (para dashboard)
    @Query("SELECT SUM(v.ventaTotal) FROM Venta v " +
            "WHERE v.usuario.id = :usuarioId " +
            "AND DATE(v.ventaFecha) = CURRENT_DATE " +
            "AND v.ventaEstado = 'completada'")
    Double findTotalVendidoHoyPorUsuario(@Param("usuarioId") Long usuarioId);
}