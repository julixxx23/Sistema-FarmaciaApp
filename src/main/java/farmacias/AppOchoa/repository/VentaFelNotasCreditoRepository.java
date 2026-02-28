package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.NotaEstado;
import farmacias.AppOchoa.model.VentaFelNotasCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaFelNotasCreditoRepository extends JpaRepository<VentaFelNotasCredito, Long> {

    List<VentaFelNotasCredito> findByNotaEstado(NotaEstado notaEstado);
    List<VentaFelNotasCredito> findByAuditoriaFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    Optional<VentaFelNotasCredito> findByNotaUuid(String uuid);
    List<VentaFelNotasCredito> findByNotaNumeroAutorizacion(String nota);

    List<VentaFelNotasCredito> findByVentaFel_FelId(Long felId);
    Page<VentaFelNotasCredito> findByNotaEstado(NotaEstado notaEstado, Pageable pageable);
    Page<VentaFelNotasCredito> findByAuditoriaFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<VentaFelNotasCredito> findByNotaEstadoAndAuditoriaFechaCreacionBetween(NotaEstado notaEstado, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}