package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
