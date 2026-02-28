package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoCreateDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoResponseDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoSimpleDTO;
import farmacias.AppOchoa.model.NotaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VentaFelNotasCreditoService {
    VentaFelNotasCreditoResponseDTO crear(VentaFelNotasCreditoCreateDTO dto);
    VentaFelNotasCreditoResponseDTO buscarPorId(Long id);
    Page<VentaFelNotasCreditoSimpleDTO> listarNotas(Pageable pageable);
    Page<VentaFelNotasCreditoSimpleDTO> buscarPorFiltros(NotaEstado estado, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    void eliminar(Long id);
}
