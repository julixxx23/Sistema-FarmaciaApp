package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.SucursalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SucursalResponseDTO obtenerPorId(Long id){
        return sucursalRepository.findById(id)
                .map(SucursalResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SucursalSimpleDTO> listarActivasPaginadas(Pageable pageable) {
        return sucursalRepository.findBySucursalEstadoTrue(pageable)
                .map(SucursalSimpleDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SucursalSimpleDTO> listarTodasPaginadas(Pageable pageable) {
        return sucursalRepository.findAll(pageable)
                .map(SucursalSimpleDTO::fromEntity);
    }

    @Override
    public SucursalResponseDTO actualizar(Long id, SucursalUpdateDTO dto){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));

        String nuevoNombre = dto.getNombre().trim();
        if(!sucursal.getSucursalNombre().equalsIgnoreCase(nuevoNombre)){
            if(sucursalRepository.existsBySucursalNombre(nuevoNombre)){
                throw new RuntimeException("Ya existe otra sucursal con ese nombre: " + nuevoNombre);
            }
            sucursal.setSucursalNombre(nuevoNombre);
        }

        sucursal.setSucursalDireccion(dto.getDireccion());
        sucursal.setSucursalTelefono(dto.getTelefono());

        if(dto.getEstado() != null){
            sucursal.setSucursalEstado(dto.getEstado());
        }

        return SucursalResponseDTO.fromEntity(sucursalRepository.save(sucursal));
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + id));
        sucursal.setSucursalEstado(estado);
        sucursalRepository.save(sucursal);
    }

    @Override
    public void eliminar(Long id){
        cambiarEstado(id, false);
    }
}