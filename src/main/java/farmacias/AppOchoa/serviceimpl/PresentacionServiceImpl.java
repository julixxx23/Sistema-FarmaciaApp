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
            throw new RuntimeException("Ya existe una presentacion con ese nombre: " +dto.getNombre());
        }

        Presentacion presentacion = Presentacion.builder()
                .presentacionNombre(dto.getNombre())
                .build();

        Presentacion guardar = presentacionRepository.save(presentacion);

        return PresentacionResponseDTO.fromEntity(guardar);

    }

    @Override
    public  PresentacionResponseDTO obtenerPorId(Long id){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentacion no encontrada por id: " + id));

        return  PresentacionResponseDTO.fromEntity(presentacion);
    }

    @Override
    public List<PresentacionSimpleDTO> listarTodas(){
        List<Presentacion>  presentaciones = presentacionRepository.findAll();

        return presentaciones.stream()
                .map(PresentacionSimpleDTO :: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public  List<PresentacionSimpleDTO> listarActivas(){
        List<Presentacion> presentacion = presentacionRepository.findByPresentacionEstadoTrue();

        return presentacion.stream()
                .map(PresentacionSimpleDTO :: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PresentacionResponseDTO actualizar(Long id, PresentacionUpdateDTO dto){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentacion no encontrada por ID: " +id));

        if(!presentacion.getPresentacionNombre().equals(dto.getNombre())){
            if(presentacionRepository.existsByPresentacionNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra presentacion con ese nombre: " +dto.getNombre());
            }
        }

        presentacion.setPresentacionNombre(dto.getNombre());

        Presentacion actualizada = presentacionRepository.save(presentacion);

        return PresentacionResponseDTO.fromEntity(actualizada);
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Presentacion presentacion = presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentacion no encontrada por ID: " +id));

        presentacion.setPresentacionEstado(estado);

        presentacionRepository.save(presentacion);
    }

    @Override
    public void eliminar(Long id){
        cambiarEstado(id, false);
    }







}
