package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.ventapago.VentaPagoCreateDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoResponseDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface VentaPagoService {
    VentaPagoResponseDTO crear(VentaPagoCreateDTO dto);
    VentaPagoResponseDTO buscarPorId(Long id);
    Page<VentaPagoSimpleDTO> listarActivas(Pageable pageable);
    void eliminar(Long id);

}
