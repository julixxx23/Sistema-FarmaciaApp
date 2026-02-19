package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.cajacorte.CajaCorteCreateDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteResponseDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CajaCorteService {
    CajaCorteResponseDTO crear(CajaCorteCreateDTO dto);
    CajaCorteResponseDTO buscarPorId(Long id);
    Page<CajaCorteSimpleDTO> listarCortes(Pageable pageable);
    void eliminar(Long id);
}
