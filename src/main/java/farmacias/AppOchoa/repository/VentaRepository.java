package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Venta;
import farmacias.AppOchoa.model.VentaEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Para reimprimir factura
    Optional<Venta> findByVentaNumeroFactura(String numeroFactura);

    // Para cierre de caja del día - CORREGIDO
    List<Venta> findBySucursal_SucursalIdAndVentaFechaBetween(
            Long sucursalId, LocalDateTime inicio, LocalDateTime fin);

    // Buscar por estado de venta
    List<Venta> findByVentaEstado(VentaEstado estado);

    // Para validar que no se repita número factura
    boolean existsByVentaNumeroFactura(String numeroFactura);

    // Para historial de cliente
    List<Venta> findByVentaNitCliente(String nitCliente);

    // Ventas por cajero específico (para rendición de cuentas) - CORREGIDO
    List<Venta> findByUsuario_UsuarioId(Long usuarioId);

    // Ventas por cajero en turno específico - CORREGIDO
    List<Venta> findByUsuario_UsuarioIdAndVentaFechaBetween(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fin);

    // Total vendido por cajero hoy (para dashboard) - CORREGIDO
    @Query("SELECT SUM(v.ventaTotal) FROM Venta v " +
            "WHERE v.usuario.usuarioId = :usuarioId " +
            "AND DATE(v.ventaFecha) = CURRENT_DATE " +
            "AND v.ventaEstado = 'completada'")
    Double findTotalVendidoHoyPorUsuario(@Param("usuarioId") Long usuarioId);
}