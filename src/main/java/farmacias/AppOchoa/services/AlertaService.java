package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.alerta.AlertaResponseDTO;
import farmacias.AppOchoa.dto.alerta.AlertaSimpleDTO;
import farmacias.AppOchoa.dto.alerta.AlertaUpdateDTO;

import java.util.List;

public interface AlertaService {

    AlertaResponseDTO listarPorId(Long id);
    List<AlertaSimpleDTO> listarTodas();
    List<AlertaSimpleDTO> listarActivas();

    AlertaResponseDTO actualizar(Long id, AlertaUpdateDTO dto);
    void cambiarEstado(Long id);
    void eliminar(Long id);
}
