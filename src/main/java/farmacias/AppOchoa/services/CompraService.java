package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.dto.compra.CompraSimpleDTO;
import farmacias.AppOchoa.dto.compra.CompraUpdateDTO;

import java.util.List;

public interface CompraService {

    CompraResponseDTO crear(CompraCreateDTO dto);

    CompraResponseDTO listarPorId(Long id);
    List<CompraSimpleDTO> listaTodas();
    List<CompraSimpleDTO> listarActivos();

    CompraResponseDTO actualizar(Long id, CompraUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);
    void eliminar(Long id);
}