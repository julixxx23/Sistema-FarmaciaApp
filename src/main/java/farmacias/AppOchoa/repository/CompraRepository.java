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

    // 1. Navegación: Compra -> sucursal -> sucursalId
    List<Compra> findBySucursalSucursalId(Long sucursalId);

    // 2. Navegación: Compra -> usuario -> usuarioId
    List<Compra> findByUsuarioUsuarioId(Long usuarioId);

    List<Compra> findByCompraFecha(LocalDate fecha);

    // 3. Usa el Enum CompraEstado en lugar de String para mayor seguridad
    List<Compra> findByCompraEstado(CompraEstado estado);

    List<Compra> findByCompraFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // 4. Corrección de nombres compuestos
    List<Compra> findBySucursalSucursalIdAndCompraEstado(Long sucursalId, CompraEstado estado);

    List<Compra> findBySucursalSucursalIdAndCompraFechaBetween(
            Long sucursalId, LocalDate fechaInicio, LocalDate fechaFin);

    // 5. El parámetro debe ser CompraEstado para coincidir con la entidad
    List<Compra> findTop10ByCompraEstadoOrderByCompraFechaDesc(CompraEstado estado);

    boolean existsBySucursalSucursalIdAndCompraFecha(Long sucursalId, LocalDate fecha);

    // 6. JPQL: Cambiado c.sucursal.id por c.sucursal.sucursalId
    @Query("SELECT MONTH(c.compraFecha) as mes, SUM(c.compraTotal) as total " +
            "FROM Compra c " +
            "WHERE YEAR(c.compraFecha) = :anio AND c.sucursal.sucursalId = :sucursalId " +
            "GROUP BY MONTH(c.compraFecha)")
    List<Object[]> findTotalComprasPorMes(@Param("anio") int anio,
                                          @Param("sucursalId") Long sucursalId);

    @Query("SELECT c FROM Compra c WHERE c.compraTotal > :montoMinimo " +
            "AND c.compraEstado = 'activa'")
    List<Compra> findComprasConMontoMayorA(@Param("montoMinimo") BigDecimal montoMinimo);
}