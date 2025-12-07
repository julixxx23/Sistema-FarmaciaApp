package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;

import java.util.List;

public interface SucursalService {

    SucursalResponseDTO crear(SucursalCreateDTO dto);

    SucursalResponseDTO obtenerPorId (Long id);
    List<SucursalSimpleDTO> listarTodas();
    List<SucursalSimpleDTO> listarActivas();

    SucursalResponseDTO actualizar(Long id, SucursalUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);

    void eliminarSucursal(Long id);




}
