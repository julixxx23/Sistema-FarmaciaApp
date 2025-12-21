package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.dto.venta.VentaSimpleDTO;
import farmacias.AppOchoa.dto.venta.VentaUpdateDTO;
import farmacias.AppOchoa.model.VentaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaService {

    VentaResponseDTO crear(VentaCreateDTO dto);

    VentaResponseDTO listarPorId(Long id);
    Page<VentaSimpleDTO> listarTodasPaginadas(Pageable pageable);
    Page<VentaSimpleDTO> listarActivasPaginadas(Pageable pageable);

    VentaResponseDTO actualizar(Long id, VentaUpdateDTO dto);
    void cambiarEstado(Long id, VentaEstado nuevoEstado);
    void eliminar(Long id);
}