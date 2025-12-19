package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SucursalService {

    // Métodos de escritura
    SucursalResponseDTO crear(SucursalCreateDTO dto);
    SucursalResponseDTO actualizar(Long id, SucursalUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);
    void eliminar(Long id);

    // Métodos de consulta
    SucursalResponseDTO obtenerPorId(Long id);

    // Métodos de paginación
    Page<SucursalSimpleDTO> listarActivasPaginadas(Pageable pageable);
    Page<SucursalSimpleDTO> listarTodasPaginadas(Pageable pageable);
}