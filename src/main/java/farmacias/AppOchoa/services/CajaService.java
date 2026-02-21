package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.dto.caja.CajaUpdateDTO;
import farmacias.AppOchoa.model.CajaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CajaService {
    CajaResponseDTO crearCaja(CajaCreateDTO dto);
    CajaResponseDTO actualizarCaja(Long id, CajaUpdateDTO dto);
    CajaResponseDTO buscarPorId(Long id);
    Page<CajaSimpleDTO> listarCajasActivas(Pageable pageable);
    void cambiarEstado(Long id, CajaEstado cajaEstado);
    void eliminar(Long id);
}
