package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // NOTA: Usamos Producto_ProductoId porque en la entidad Producto el campo es productoId
    List<Alerta> findByProducto_ProductoId(Long productoId);

    List<Alerta> findByAlertaLeidaFalse();
}