package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface InventarioLotesService {
    InventarioLotesResponseDTO crear(InventarioLotesCreateDTO dto);
    Page<InventarioLotesResponseDTO> listarPorSucursalPaginado(Long sucursalId, Pageable pageable);
    InventarioLotesResponseDTO buscarPorId(Long id);
    InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto);
    Page<InventarioLotesResponseDTO> listarProximosAVencerPaginado(LocalDate fechaLimite, Pageable pageable);
    void eliminar(Long id);
}