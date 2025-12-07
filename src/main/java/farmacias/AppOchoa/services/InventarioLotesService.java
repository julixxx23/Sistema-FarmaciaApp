package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;

import java.util.List;

public interface InventarioLotesService {

    InventarioLotesResponseDTO crear (InventarioLotesCreateDTO dto);

    InventarioLotesResponseDTO listarPorId(Long id);
    List<InventarioLotesSimpleDTO> listarTodos();
    List<InventarioLotesSimpleDTO> listarActivos();

    InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);
    void eliminar(Long id);
}
