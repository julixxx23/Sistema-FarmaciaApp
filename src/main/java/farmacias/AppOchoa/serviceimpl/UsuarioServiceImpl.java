package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.usuario.*;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              SucursalRepository sucursalRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByNombreUsuarioUsuario(dto.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario '" + dto.getNombreUsuario() + "' ya está en uso");
        }

        Sucursal sucursal = null;
        if (dto.getSucursalId() != null) {
            sucursal = buscarSucursal(dto.getSucursalId());
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuarioUsuario(dto.getNombreUsuario())
                .usuarioContrasenaHash(passwordEncoder.encode(dto.getContrasena()))
                .usuarioNombre(dto.getNombre())
                .usuarioApellido(dto.getApellido())
                .usuarioRol(dto.getRol())
                .usuarioEstado(true)
                .sucursal(sucursal)
                .build();

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioSimpleDTO> listarUsuariosActivosPaginado(Pageable pageable) {
        return usuarioRepository.findByUsuarioEstadoTrue(pageable)
                .map(UsuarioSimpleDTO::fromEntity);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado ID: " + id));

        // Validar nombre de usuario si cambia
        if (!usuario.getNombreUsuarioUsuario().equals(dto.getNombreUsuario())) {
            if (usuarioRepository.existsByNombreUsuarioUsuario(dto.getNombreUsuario())) {
                throw new ResourceNotFoundException("El nuevo nombre de usuario ya está siendo usado por otra cuenta");
            }
            usuario.setNombreUsuarioUsuario(dto.getNombreUsuario());
        }

        // Manejo de sucursal opcional
        if (dto.getSucursalId() != null) {
            Sucursal sucursal = buscarSucursal(dto.getSucursalId());
            usuario.setSucursal(sucursal);
        } else {
            usuario.setSucursal(null);
        }

        usuario.setUsuarioNombre(dto.getNombre());
        usuario.setUsuarioApellido(dto.getApellido());
        usuario.setUsuarioRol(dto.getRol());
        usuario.setUsuarioEstado(dto.getEstado());

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    public void cambiarEstado(Long id, Boolean nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado ID: " + id));
        usuario.setUsuarioEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        cambiarEstado(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByNombreUsuarioUsuario(dto.getNombreUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getUsuarioContrasenaHash())) {
            throw new ResourceNotFoundException("Credenciales inválidas");
        }

        if (Boolean.FALSE.equals(usuario.getUsuarioEstado())) {
            throw new ResourceNotFoundException("La cuenta de usuario está desactivada");
        }

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // Método auxiliar privado
    private Sucursal buscarSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada ID: " + id));
    }
}