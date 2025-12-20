package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.alerta.AlertaCreateDTO;
import farmacias.AppOchoa.dto.alerta.AlertaResponseDTO;
import farmacias.AppOchoa.dto.alerta.AlertaSimpleDTO;
import farmacias.AppOchoa.dto.alerta.AlertaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertaService {

    AlertaResponseDTO crear(AlertaCreateDTO dto);
    AlertaResponseDTO listarPorId(Long id);

    Page<AlertaSimpleDTO> listarTodasPaginadas(Pageable pageable);
    Page<AlertaSimpleDTO> listarNoLeidasPaginadas(Pageable pageable);

    AlertaResponseDTO actualizar(Long id, AlertaUpdateDTO dto);
    void cambiarEstado(Long id);
    void eliminar(Long id);
}