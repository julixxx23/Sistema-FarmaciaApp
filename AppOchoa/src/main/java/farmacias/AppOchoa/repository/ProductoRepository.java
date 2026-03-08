package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByProductoNombre(String productoNombre);
    boolean existsByProductoCodigoBarras(String productoCodigoBarras);
    Optional<Producto> findByProductoCodigoBarras(String productoCodigoBarras);
    Page<Producto> findByProductoEstadoTrue(Pageable pageable);

    boolean existsByFarmacia_FarmaciaIdAndProductoNombre(Long farmaciaId, String productoNombre);
    boolean existsByFarmacia_FarmaciaIdAndProductoCodigoBarras(Long farmaciaId, String codigoBarras);
    Optional<Producto> findByFarmacia_FarmaciaIdAndProductoCodigoBarras(Long farmaciaId, String codigoBarras);
    Page<Producto> findByFarmacia_FarmaciaIdAndProductoEstadoTrue(Long farmaciaId, Pageable pageable);
}