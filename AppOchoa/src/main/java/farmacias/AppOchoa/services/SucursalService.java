package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SucursalService {

    SucursalResponseDTO crear(Long farmaciaId, SucursalCreateDTO dto);
    SucursalResponseDTO actualizar(Long farmaciaId, Long id, SucursalUpdateDTO dto);
    void cambiarEstado(Long farmaciaId, Long id, Boolean estado);
    void eliminar(Long farmaciaId, Long id);
    // Métodos de consulta
    SucursalResponseDTO obtenerPorId(Long farmaciaId, Long id);
    // Métodos de paginación
    Page<SucursalSimpleDTO> listarActivasPaginadas(Long farmaciaId, Pageable pageable);
    Page<SucursalSimpleDTO> listarTodasPaginadas(Long farmaciaId, Pageable pageable);
}