package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.ventapago.VentaPagoCreateDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoResponseDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoSimpleDTO;
import org.springframework.data.domain.Page;

public interface VentaPagoService {
    VentaPagoResponseDTO crear(VentaPagoCreateDTO dto);
    VentaPagoResponseDTO buscarPorId(Long id);
    Page<VentaPagoSimpleDTO> listarActivas(Long id);
    void eliminar(Long id);

}
