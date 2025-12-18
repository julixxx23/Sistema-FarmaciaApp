package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionSimpleDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionUpdateDTO;
import farmacias.AppOchoa.model.Presentacion;
import farmacias.AppOchoa.repository.PresentacionRepository;
import farmacias.AppOchoa.services.PresentacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PresentacionServiceImpl implements PresentacionService {

    private final PresentacionRepository presentacionRepository;

    public PresentacionServiceImpl(PresentacionRepository presentacionRepository){
        this.presentacionRepository = presentacionRepository;
    }

    @Override
    public PresentacionResponseDTO crear(PresentacionCreateDTO dto){
        if(presentacionRepository.existsByPresentacionNombre(dto.getNombre())){
            throw new RuntimeException("Ya existe una presentación con ese nombre: " + dto.getNombre());
        }

        Presentacion presentacion = Presentacion.builder()
                .presentacionNombre(dto.getNombre())
                .presentacionEstado(true) // CORRECCIÓN: Asegurar que nazca activa
                .build();

        return PresentacionResponseDTO.fromEntity(presentacionRepository.save(presentacion));
    }

    @Override
    public PresentacionResponseDTO obtenerPorId(Long id){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada por ID: " + id));
        return PresentacionResponseDTO.fromEntity(presentacion);
    }

    @Override
    public List<PresentacionSimpleDTO> listarTodas(){
        return presentacionRepository.findAll().stream()
                .map(PresentacionSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PresentacionSimpleDTO> listarActivas(){
        // Verifica que tu repositorio tenga: List<Presentacion> findByPresentacionEstadoTrue();
        return presentacionRepository.findByPresentacionEstadoTrue().stream()
                .map(PresentacionSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PresentacionResponseDTO actualizar(Long id, PresentacionUpdateDTO dto){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada por ID: " + id));

        // Validar si el nombre cambia y si el nuevo ya existe
        if(!presentacion.getPresentacionNombre().equalsIgnoreCase(dto.getNombre())){
            if(presentacionRepository.existsByPresentacionNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra presentación con el nombre: " + dto.getNombre());
            }
        }

        presentacion.setPresentacionNombre(dto.getNombre());

        // CORRECCIÓN: Si el DTO de update tiene campo 'estado', actualízalo aquí
        if (dto.getEstado() != null) {
            presentacion.setPresentacionEstado(dto.getEstado());
        }

        return PresentacionResponseDTO.fromEntity(presentacionRepository.save(presentacion));
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada por ID: " + id));
        presentacion.setPresentacionEstado(estado);
        presentacionRepository.save(presentacion);
    }

    @Override
    public void eliminar(Long id){
        // Aplicamos eliminación lógica
        cambiarEstado(id, false);
    }
}