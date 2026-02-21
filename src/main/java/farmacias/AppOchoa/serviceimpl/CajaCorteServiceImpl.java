package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.cajacorte.CajaCorteCreateDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteResponseDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteSimpleDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.CajaCorte;
import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.CajaCortesRepository;
import farmacias.AppOchoa.repository.CajaSesionesRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.CajaCorteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaCorteServiceImpl implements CajaCorteService {
    private final CajaCortesRepository cajaCortesRepository;
    private final CajaSesionesRepository cajaSesionesRepository;
    private final UsuarioRepository usuarioRepository;

    public CajaCorteServiceImpl(
            CajaCortesRepository cajaCortesRepository,
            CajaSesionesRepository cajaSesionesRepository,
            UsuarioRepository usuarioRepository) {
        this.cajaCortesRepository = cajaCortesRepository;
        this.cajaSesionesRepository = cajaSesionesRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public CajaCorteResponseDTO crear(CajaCorteCreateDTO dto) {
    CajaSesiones cajaSesiones = buscarSesiones(dto.getSesionId());
    Usuario usuario = buscarUsuario(dto.getUsuarioSupervisorId());

    CajaCorte cajaCorte = CajaCorte.builder()
            .cajaSesiones(cajaSesiones)
            .corteTotalEfectivo(dto.getEfectivoFisicoContado())
            .usuario(usuario)
            .build();

    return CajaCorteResponseDTO.fromEntity(cajaCortesRepository.save(cajaCorte));

}
//Metodos Auxiliares
private CajaSesiones buscarSesiones(Long id) {
    if (id == null) return null;
    return cajaSesionesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sesion no encontrada por ID"));
}
private Usuario buscarUsuario(Long id){
    if(id == null) return null;
    return usuarioRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado por ID"));
    }

    @Override
    @Transactional(readOnly = true)
    public CajaCorteResponseDTO buscarPorId(Long id){
        return cajaCortesRepository.findById(id)
                .map(CajaCorteResponseDTO:: fromEntity)
                .orElseThrow(()-> new ResourceNotFoundException("Corte no encontrado por ID"));
    }
    @Override
    @Transactional(readOnly = true)
    public Page<CajaCorteSimpleDTO> listarCortes(Pageable pageable){
        return cajaCortesRepository.findAll(pageable)
                .map(CajaCorteSimpleDTO::fromEntity);
    }


}



