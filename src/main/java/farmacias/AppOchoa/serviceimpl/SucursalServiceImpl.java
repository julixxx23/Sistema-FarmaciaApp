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
            throw new RuntimeException("Ya existe una sucursal con ese nombre: " + dto.getNombre());
        }

        Sucursal sucursal = Sucursal.builder()
                .sucursalNombre(dto.getNombre())
                .sucursalDireccion(dto.getDireccion())
                .sucursalTelefono(dto.getTelefono())
                .sucursalEstado(true)
                .build();

        return SucursalResponseDTO.fromEntity(sucursalRepository.save(sucursal));
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponseDTO obtenerPorId (Long id){
        return sucursalRepository.findById(id)
                .map(SucursalResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalSimpleDTO> listarTodas(){
        return sucursalRepository.findAll().stream()
                .map(SucursalSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalSimpleDTO> listarActivas(){
        return sucursalRepository.findBySucursalEstadoTrue().stream()
                .map(SucursalSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SucursalResponseDTO actualizar(Long id, SucursalUpdateDTO dto){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));

        if(!sucursal.getSucursalNombre().equalsIgnoreCase(dto.getNombre())){
            if(sucursalRepository.existsBySucursalNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra sucursal con ese nombre: " + dto.getNombre());
            }
        }

        // CORRECCIÓN: Actualizar todos los campos necesarios
        sucursal.setSucursalNombre(dto.getNombre());
        sucursal.setSucursalDireccion(dto.getDireccion()); // No olvides la dirección
        sucursal.setSucursalTelefono(dto.getTelefono());   // No olvides el teléfono
        sucursal.setSucursalEstado(dto.getEstado());

        return SucursalResponseDTO.fromEntity(sucursalRepository.save(sucursal));
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));

        // CORRECCIÓN: Antes tenías sucursal.setSucursalEstado(true);
        // Eso hacía que siempre se activara, ignorando el parámetro.
        sucursal.setSucursalEstado(estado);

        sucursalRepository.save(sucursal);
    }

    @Override
    public void eliminarSucursal(Long id){
        // Aplicamos el borrado lógico
        cambiarEstado(id, false);
    }
}