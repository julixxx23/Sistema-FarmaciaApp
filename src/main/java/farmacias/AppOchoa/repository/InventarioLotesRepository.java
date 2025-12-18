package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.LoteEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioLotesRepository extends JpaRepository<InventarioLotes, Long> {

    // BÚSQUEDAS BÁSICAS - Navegación: Producto -> productoId y Sucursal -> sucursalId
    Optional<InventarioLotes> findByProducto_ProductoIdAndSucursal_SucursalId(Long productoId, Long sucursalId);

    List<InventarioLotes> findAllByProducto_ProductoIdAndSucursal_SucursalId(Long productoId, Long sucursalId);

    List<InventarioLotes> findBySucursal_SucursalId(Long sucursalId);

    // VALIDACIONES - Estos nombres deben coincidir exactamente con el Service
    boolean existsByProducto_ProductoIdAndSucursal_SucursalId(Long productoId, Long sucursalId);

    // Este es el método que causaba el error en la línea 41
    boolean existsByLoteNumeroAndSucursal_SucursalId(String loteNumero, Long sucursalId);

    // ESTADO Y STOCK
    List<InventarioLotes> findByLoteEstado(LoteEstado estado);

    List<InventarioLotes> findByLoteCantidadActualLessThan(Integer cantidadMinima);

    // FECHAS Y VENCIMIENTOS
    List<InventarioLotes> findByLoteFechaVencimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<InventarioLotes> findByLoteFechaVencimientoLessThanEqual(LocalDate fechaLimite);

    // ESCÁNER / CÓDIGO
    Optional<InventarioLotes> findByLoteNumero(String loteNumero);
}