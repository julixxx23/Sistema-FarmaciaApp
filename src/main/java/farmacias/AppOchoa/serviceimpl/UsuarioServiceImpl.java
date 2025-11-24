package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.usuario.*;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              SucursalRepository sucursalRepository,
                              PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto){
        if(usuarioRepository.existsByNombreUsuarioUsuario(dto.getNombreUsuario())){
            throw new RuntimeException("El nombre del usuario ya esta en uso");
        }

        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Usuario usuario = Usuario.builder()
                .nombreUsuarioUsuario(dto.getNombreUsuario())
                .usuarioContrasenaHash(passwordEncoder.encode(dto.getContrasena()))
                .usuarioNombre(dto.getNombre())
                .usuarioApellido(dto.getApellido())
                .usuarioRol(dto.getRol())
                .usuarioEstado(true)
                .sucursal(sucursal)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(guardado);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        usuario.setUsuarioNombre(dto.getNombre());
        usuario.setUsuarioApellido(dto.getApellido());
        usuario.setUsuarioRol(dto.getRol());
        usuario.setUsuarioEstado(dto.getEstado());
        usuario.setSucursal(sucursal);

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(actualizado);

    }

    @Override
    public void eliminarUsuario(Long id){
        if(!usuarioRepository.existsById(id)){
            throw  new RuntimeException("Usuario no encontrado");

        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public void cambiarEstado(Long id, Boolean nuevoEstado){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsuarioEstado(nuevoEstado);
        usuarioRepository.save(usuario);

    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Override
    public List<UsuarioSimpleDTO> listarUsuariosActivos(){
        return usuarioRepository.findByUsuarioEstadoTrue()
                .stream()
                .map(UsuarioSimpleDTO::fromEntity)
                .toList();
    }

    @Override
    public UsuarioResponseDTO login(LoginDTO dto){
        Usuario usuario = usuarioRepository.findByNombreUsuarioUsuario(dto.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if(!passwordEncoder.matches(dto.getContrasena(), usuario.getUsuarioContrasenaHash())){
            throw new RuntimeException("Credenciales invalidas");
        }

        if(Boolean.FALSE.equals(usuario.getUsuarioEstado())){
            throw new RuntimeException("Usuario inactivo");
        }

        return UsuarioResponseDTO.fromEntity(usuario);
    }






}
