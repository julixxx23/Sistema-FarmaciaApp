package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;

import java.util.List;

public interface InventarioService {

    InventarioResponseDTO crear (InventarioLotesCreateDTO dto);

    InventarioResponseDTO listaPorId (Long id);
    List<InventarioLotesSimpleDTO> listarTodos();
    List<InventarioLotesSimpleDTO> listarActivos();

    InventarioResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto);
    void cambiaEstado(Long id, Boolean estado);
    void eliminar(Long id);





}
