package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByProductoNombre(String productoNombre);
    Optional<Producto> findByProductoCodigoBarras(String productoCodigoBarras);
    boolean existsByProductoCodigoBarras(String productoCodigoBarras);
    Page<Producto> findByProductoEstadoTrue(Pageable pageable);
}