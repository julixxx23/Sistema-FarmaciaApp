package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesCreateDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesResponseDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesSimpleDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.Caja;
import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.CajaRepository;
import farmacias.AppOchoa.repository.CajaSesionesRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.CajaSesionesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaSesionesServiceImpl implements CajaSesionesService {
    private final CajaSesionesRepository cajaSesionesRepository;
    private final UsuarioRepository usuarioRepository;
    private final CajaRepository cajaRepository;

    public CajaSesionesServiceImpl(
            CajaSesionesRepository cajaSesionesRepository,
            UsuarioRepository usuarioRepository,
            CajaRepository cajaRepository){
        this.cajaSesionesRepository = cajaSesionesRepository;
        this.usuarioRepository = usuarioRepository;
        this.cajaRepository = cajaRepository;
    }
    @Override
    public CajaSesionesResponseDTO crear(CajaSesionesCreateDTO dto){
        Usuario usuario = buscarUsuario(dto.getUsuarioId());
        Caja caja = buscarCaja(dto.getCajaId());

        CajaSesiones cajaSesiones = CajaSesiones.builder()
                .caja(caja)
                .usuario(usuario)
                .sesionFondoInicial(dto.getSesionFondoInicial())
                .build();

        return CajaSesionesResponseDTO.fromEntity(cajaSesionesRepository.save(cajaSesiones));

    }
    //Metodos Auxiliares
    private Usuario buscarUsuario(Long id){
        if(id == null) return null;
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado por ID"));
    }
    private Caja buscarCaja(Long id){
        if(id == null) return null;
        return cajaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Sesion no encontrado por ID"));
    }
    @Transactional(readOnly = true)
    @Override
    public CajaSesionesResponseDTO buscarPorId(Long id){
        return cajaSesionesRepository.findById(id)
                .map(CajaSesionesResponseDTO::fromEntity)
                .orElseThrow(()-> new ResourceNotFoundException("Caja no encontrada por ID"));
    }
    @Transactional(readOnly = true)
    @Override
    public Page<CajaSesionesSimpleDTO> listarSesiones(Pageable pageable){
        return cajaSesionesRepository.findAll(pageable)
                .map(CajaSesionesSimpleDTO::fromEntity);
    }
    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Por reglas de auditoría financiera, este registro es histórico y no puede ser eliminado ni modificado.");
    }
    }






