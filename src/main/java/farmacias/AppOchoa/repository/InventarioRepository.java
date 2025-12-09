package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    //Para ajustar stock y transferir
    Optional<Inventario> findByIdProductoAndSucursalId(Long productoId, Long sucursalId);

    // Para consultar stock bajo mínimo
    List<Inventario> findByInventarioCantidadActualLessThanEqual(Integer cantidad);

    // Para buscar en sucursal específica
    List<Inventario> findBySucursalId(Long sucursalId);

    // Para evitar productos duplicados
    boolean existsByProductoIdAndSucursalId(Long productoId, Long sucursalId);

}
