package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import java.time.LocalDate;
import java.util.List;

public interface InventarioLotesService {
    InventarioLotesResponseDTO crear(InventarioLotesCreateDTO dto);
    List<InventarioLotesResponseDTO> listarPorSucursal(Long sucursalId);
    InventarioLotesResponseDTO buscarPorId(Long id);
    InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto);
    List<InventarioLotesResponseDTO> listarProximosAVencer(LocalDate fechaLimite);
    void eliminar(Long id);
}