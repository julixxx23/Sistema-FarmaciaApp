package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionSimpleDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PresentacionService {

    // Métodos de escritura
    PresentacionResponseDTO crear(PresentacionCreateDTO dto);
    PresentacionResponseDTO actualizar(Long id, PresentacionUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);
    void eliminar(Long id);

    // Métodos de consulta simple
    PresentacionResponseDTO obtenerPorId(Long id);
    List<PresentacionSimpleDTO> listarTodas();
    List<PresentacionSimpleDTO> listarActivas();

    // Métodos de paginación
    Page<PresentacionSimpleDTO> listarActivasPaginadas(Pageable pageable);
    Page<PresentacionSimpleDTO> listarTodasPaginadas(Pageable pageable);
}