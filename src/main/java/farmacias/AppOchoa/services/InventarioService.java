package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventario.InventarioUpdateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import farmacias.AppOchoa.model.Inventario;

import java.util.List;

public interface InventarioService {

    InventarioResponseDTO crear (InventarioCreateDTO dto);

    InventarioResponseDTO listaPorId (Long id);
    List<InventarioSimpleDTO> listarTodos();
    List<InventarioSimpleDTO> listarActivos();

    InventarioResponseDTO actualizar(Long id, InventarioUpdateDTO dto);
    void cambiaEstado(Long id, Boolean estado);
    void eliminar(Long id);





}
