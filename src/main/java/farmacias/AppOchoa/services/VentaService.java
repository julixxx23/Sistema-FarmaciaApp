package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.dto.venta.VentaSimpleDTO;
import farmacias.AppOchoa.dto.venta.VentaUpdateDTO;
import farmacias.AppOchoa.model.VentaEstado;

import java.util.List;

public interface VentaService {

    VentaResponseDTO crear(VentaCreateDTO dto);

    VentaResponseDTO listarPorId(Long id);
    List<VentaSimpleDTO> listarTodas();
    List<VentaSimpleDTO> listarActivas();

    VentaResponseDTO actualizar(Long id, VentaUpdateDTO dto);
    void cambiarEstado(Long id, VentaEstado nuevoEstado);
    void eliminar(Long id);
}
