package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Compra;
import farmacias.AppOchoa.model.CompraEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    //Compras por sucursal
    List<Compra> findBySucursalId(Long sucursalId);

    //Compras por usuario (quien registró)
    List<Compra> findByUsuarioId(Long usuarioId);

    //Compras por fecha exacta
    List<Compra> findByCompraFecha(LocalDate fecha);

    List<Compra> findByCompraEstado(CompraEstado estado);

    //Compras por rango de fechas (para reportes)
    List<Compra> findByCompraFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    //Compras por sucursal y estado
    List<Compra> findBySucursalIdAndCompraEstado(Long sucursalId, String estado);

    //Compras por sucursal en rango de fechas
    List<Compra> findBySucursalIdAndCompraFechaBetween(
            Long sucursalId, LocalDate fechaInicio, LocalDate fechaFin);

    //Compras activas recientes (para dashboard)
    List<Compra> findTop10ByCompraEstadoOrderByCompraFechaDesc(String estado);

    //Verificar si existen compras para una sucursal en fecha
    boolean existsBySucursalIdAndCompraFecha(Long sucursalId, LocalDate fecha);

    //Consulta personalizada para total de compras por mes
    @Query("SELECT MONTH(c.compraFecha) as mes, SUM(c.compraTotal) as total " +
            "FROM Compra c " +
            "WHERE YEAR(c.compraFecha) = :anio AND c.sucursal.id = :sucursalId " +
            "GROUP BY MONTH(c.compraFecha)")
    List<Object[]> findTotalComprasPorMes(@Param("anio") int anio,
                                          @Param("sucursalId") Long sucursalId);

    //Consulta para compras con total mayor a X (para auditoría)
    @Query("SELECT c FROM Compra c WHERE c.compraTotal > :montoMinimo " +
            "AND c.compraEstado = 'activa'")
    List<Compra> findComprasConMontoMayorA(@Param("montoMinimo") BigDecimal montoMinimo);

    List<Compra> findByCompraEstadoTrue();
}