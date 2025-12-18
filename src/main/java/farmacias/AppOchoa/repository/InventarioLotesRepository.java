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

    // BÚSQUEDAS BÁSICAS Y AJUSTES
    Optional<InventarioLotes> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    List<InventarioLotes> findAllByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    List<InventarioLotes> findBySucursalId(Long sucursalId);

    //VALIDACIONES DE EXISTENCIA
    boolean existsByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    boolean existsByLoteNumeroAndSucursalId(String loteNumero, Long sucursalId);


    List<InventarioLotes> findByLoteEstado(LoteEstado estado);

    List<InventarioLotes> findByLoteCantidadActualLessThan(Integer cantidadMinima);

    //FECHAS Y VENCIMIENTOS
    List<InventarioLotes> findByLoteFechaVencimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<InventarioLotes> findByLoteFechaVencimientoLessThanEqual(LocalDate fechaLimite);

    //BÚSQUEDA POR ESCÁNER O CÓDIGO
    Optional<InventarioLotes> findByLoteNumero(String loteNumero);
}