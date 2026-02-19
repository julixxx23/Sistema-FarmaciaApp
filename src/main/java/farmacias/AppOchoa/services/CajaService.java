package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.dto.caja.CajaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CajaService {
    CajaResponseDTO crearCaja(CajaCreateDTO dto);
    CajaResponseDTO actualizarCaja(CajaUpdateDTO dto);
    CajaResponseDTO buscarPorId(Long id);
    Page<CajaSimpleDTO> listarCajasActivas(Pageable pageable);
    void eliminarCaja(Long id);

}
