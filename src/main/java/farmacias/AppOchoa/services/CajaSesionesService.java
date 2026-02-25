package farmacias.AppOchoa.services;


import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesCreateDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesResponseDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CajaSesionesService {
    CajaSesionesResponseDTO crear(CajaSesionesCreateDTO dto);
    CajaSesionesResponseDTO buscarPorId(Long id);
    Page<CajaSesionesSimpleDTO> listarSesiones(Pageable pageable);
    void eliminar(Long id);
}
