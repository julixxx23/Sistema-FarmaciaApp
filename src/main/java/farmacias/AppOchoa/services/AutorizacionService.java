package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.autorizacion.AutorizacionCreateDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionResponseDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface AutorizacionService {
    AutorizacionResponseDTO crear(AutorizacionCreateDTO dto);
    AutorizacionResponseDTO buscarPorId(Long id);
    Page<AutorizacionSimpleDTO> listarTodas(Pageable pageable);
    void eliminar(Long id);
}
