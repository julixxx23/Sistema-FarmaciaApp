package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByProductoCodigoBarras(String productoCodigoBarras);

    boolean existsByProductoNombre(String productoNombre);

    List<Producto> findByProductoEstadoTrue();
}
