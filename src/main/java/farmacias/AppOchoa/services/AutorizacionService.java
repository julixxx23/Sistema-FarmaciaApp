package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.autorizacion.AutorizacionCreateDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionResponseDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionSimpleDTO;
import org.springframework.data.domain.Page;

public interface AutorizacionService {
    AutorizacionResponseDTO crear(AutorizacionCreateDTO dto);
    AutorizacionResponseDTO buscarPorId(Long id);
    Page<AutorizacionSimpleDTO> listarActivas(Long id);
    void eliminar(Long id);
}
