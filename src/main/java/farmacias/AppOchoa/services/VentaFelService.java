package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.ventafel.VentaFelCreateDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelResponseDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaFelService {
    VentaFelResponseDTO crear(VentaFelCreateDTO dto);
    VentaFelResponseDTO buscarPorId(Long id);
    Page<VentaFelSimpleDTO> listarActivas(Pageable pageable);
    Page<VentaFelSimpleDTO> buscarPorTermino(String termino, Pageable pageable);
    void eliminar(Long id);
}
