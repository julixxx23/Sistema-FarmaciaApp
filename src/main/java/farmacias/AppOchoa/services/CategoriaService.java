package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaUpdateDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaResponseDTO crear(CategoriaCreateDTO dto);

    CategoriaResponseDTO obtenerPorId(Long id);
    List<CategoriaSimpleDTO> listarTodas();
    List<CategoriaSimpleDTO> listarActivas();

    CategoriaResponseDTO actualizar(Long id, CategoriaUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);

    void eliminar(Long id);


}
