package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.VentaFel;
import farmacias.AppOchoa.model.VentaFelEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaFelRepository extends JpaRepository<VentaFel, Long> {

    List<VentaFel> findByFelEstado(VentaFelEstado ventaFelEstado);
    List<VentaFel> findByAuditoriaFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<VentaFel> findByFelEstadoAndFelIntentosLessThan(VentaFelEstado estado, Integer maxIntentos);

    //Búsqueda directa por UUID (de VentaFel)
    Page<VentaFel> findByFelUuidContainingIgnoreCase(String felUuid, Pageable pageable);

    // 2. Búsqueda directa por Autorización (de VentaFel)
    Page<VentaFel> findByFelNumeroAutorizacionContainingIgnoreCase(String autorizacion, Pageable pageable);

    //Búsqueda cruzada por NIT (Navega hacia Venta -> ventaNitCliente)
    Page<VentaFel> findByVenta_VentaNitClienteContainingIgnoreCase(String nit, Pageable pageable);

    Page<VentaFel> findByFelUuidContainingIgnoreCaseOrFelNumeroAutorizacionContainingIgnoreCaseOrVenta_VentaNitClienteContainingIgnoreCase(
            String param1, String param2, String param3, Pageable pageable);
}