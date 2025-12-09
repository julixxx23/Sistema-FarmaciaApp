package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    //Para alertas NO leídas
    List<Alerta> findByAlertaLeidaFalse();

    //Para alertas por tipo (stock_bajo, vencido, etc.)
    List<Alerta> findByAlertaTipo(String tipo);

    //Para alertas NO leídas de un tipo específico
    List<Alerta> findByAlertaTipoAndAlertaLeidaFalse(String tipo);

    //Para alertas recientes (últimas 24 horas)
    List<Alerta> findByAlertaFechaAfter(LocalDateTime fecha);

    //Para alertas de un producto específico
    List<Alerta> findByProductoId(Long productoId);

    //Para alertas de una sucursal específica
    List<Alerta> findBySucursalId(Long sucursalId);

    //Para contar alertas no leídas
    long countByAlertaLeidaFalse();

}
