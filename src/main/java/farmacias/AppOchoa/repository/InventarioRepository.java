package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Buscar inventario por producto y sucursal
    Optional<Inventario> findByProducto_ProductoIdAndSucursal_SucursalId(Long productoId, Long sucursalId);

    // Consultar stock bajo o igual a cierta cantidad - CORREGIDO con @Query
    @Query("SELECT i FROM Inventario i WHERE i.inventarioCantidadActual <= :cantidad")
    List<Inventario> findByInventarioCantidadActualLessThanEqual(@Param("cantidad") Integer cantidad);

    // Buscar todos los inventarios de una sucursal específica
    List<Inventario> findBySucursal_SucursalId(Long sucursalId);

    // Verificar si existe un producto en una sucursal
    boolean existsByProducto_ProductoIdAndSucursal_SucursalId(Long productoId, Long sucursalId);

    // Buscar productos con stock bajo el mínimo - CORREGIDO con @Query
    @Query("SELECT i FROM Inventario i WHERE i.inventarioCantidadActual < i.inventarioCantidadMinima")
    List<Inventario> findByInventarioCantidadActualLessThanInventarioCantidadMinima();

    // Buscar por ID de inventario
    Optional<Inventario> findByInventarioId(Long inventarioId);
}