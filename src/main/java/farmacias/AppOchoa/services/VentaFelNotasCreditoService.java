package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoCreateDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoResponseDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaFelNotasCreditoService {
    VentaFelNotasCreditoResponseDTO crear(VentaFelNotasCreditoCreateDTO dto);
    VentaFelNotasCreditoResponseDTO buscarPorId(Long id);
    Page<VentaFelNotasCreditoSimpleDTO> listarNotas(Pageable pageable);
    void eliminar(Long id);
}
