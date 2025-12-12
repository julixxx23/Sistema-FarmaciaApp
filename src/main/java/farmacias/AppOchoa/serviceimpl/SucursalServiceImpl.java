package farmacias.AppOchoa.serviceimpl;


import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.SucursalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalServiceImpl(SucursalRepository sucursalRepository){
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public SucursalResponseDTO crear(SucursalCreateDTO dto){
        if(sucursalRepository.existsBySucursalNombre(dto.getNombre())){
            throw new RuntimeException("Ya existe una sucursal con ese nombre: " +dto.getNombre());
        }

        Sucursal sucursal = Sucursal.builder()
                .sucursalNombre(dto.getNombre())
                .sucursalDireccion(dto.getDireccion())
                .sucursalTelefono(dto.getTelefono())
                .sucursalEstado(true)
                .build();

        Sucursal guardar = sucursalRepository.save(sucursal);

        return SucursalResponseDTO.fromEntity(guardar);


    }

    @Override
    public SucursalResponseDTO obtenerPorId (Long id){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " +id));

        return SucursalResponseDTO.fromEntity(sucursal);


    }
    @Override
    public List<SucursalSimpleDTO> listarTodas(){
        List<Sucursal> sucursales = sucursalRepository.findAll();

        return sucursales.stream()
                .map(SucursalSimpleDTO:: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SucursalSimpleDTO> listarActivas(){
        List<Sucursal> sucursales = sucursalRepository.findBySucursalEstadoTrue();

        return sucursales.stream()
                .map(SucursalSimpleDTO:: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SucursalResponseDTO actualizar(Long id, SucursalUpdateDTO dto){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " +id));

        if(!sucursal.getSucursalNombre().equals(dto.getNombre())){
            if(sucursalRepository.existsBySucursalNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra sucursal con ese nombre: " +dto.getNombre());
            }
        }

        sucursal.setSucursalNombre(dto.getNombre());
        sucursal.setSucursalEstado(dto.getEstado());

        Sucursal guardar = sucursalRepository.save(sucursal);

        return SucursalResponseDTO.fromEntity(guardar);

    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));

        sucursal.setSucursalEstado(true);

        sucursalRepository.save(sucursal);
    }

    @Override
    public void eliminarSucursal(Long id){
        cambiarEstado(id, false);
    }

















}
