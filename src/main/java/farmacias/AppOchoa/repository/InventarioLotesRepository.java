package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.InventarioLotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventarioLotesRepository extends JpaRepository<InventarioLotes, Long> {

    // Para ajustar lotes
    Optional<InventarioLotes> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    // Para listar TODOS los lotes de un producto en sucursal
    List<InventarioLotes> findAllByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    // Consultar lotes con stock bajo
    List<InventarioLotes> findByLoteCantidadActualLessThan(Integer cantidadMinima);

    // Para buscar en sucursal específica
    List<InventarioLotes> findBySucursalId(Long sucursalId);

    // Para evitar lotes duplicados
    boolean existsByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    // Para alertas de vencimiento
    List<InventarioLotes> findByLoteFechaVencimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Para alertas de próximos a vencer
    List<InventarioLotes> findByLoteFechaVencimientoLessThanEqual(LocalDate fechaLimite);

    // Para cuando escanean un lote
    Optional<InventarioLotes> findByLoteNumero(String loteNumero);

    // Para buscar por estado de lote
    List<InventarioLotes> findByLoteEstado(String estado);

    List<InventarioLotes> findByInventarioLoteEstadoTrue();
}