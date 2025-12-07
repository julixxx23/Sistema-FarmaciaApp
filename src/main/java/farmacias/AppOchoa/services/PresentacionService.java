package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionSimpleDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionUpdateDTO;

import java.util.List;

public interface PresentacionService {

    PresentacionResponseDTO crear(PresentacionCreateDTO dto);

    PresentacionResponseDTO obtenerPorId(Long id);
    List<PresentacionSimpleDTO> listarTodas();
    List<PresentacionSimpleDTO> listarActivas();

    PresentacionResponseDTO actualizar(Long id, PresentacionUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);

    void eliminar(Long id);


}
