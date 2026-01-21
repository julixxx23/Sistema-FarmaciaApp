package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventario.InventarioUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventarioService {

    InventarioResponseDTO crear(InventarioCreateDTO dto);

    InventarioResponseDTO listaPorId(Long id);
    Page<InventarioSimpleDTO> listarTodosPaginado(Pageable pageable);
    Page<InventarioSimpleDTO> listarActivosPaginado(Pageable pageable);

    InventarioResponseDTO actualizar(Long id, InventarioUpdateDTO dto);
    void eliminar(Long id);
}