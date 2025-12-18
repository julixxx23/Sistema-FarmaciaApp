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
import java.util.stream.Collectors;

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
            throw new RuntimeException("El nombre de usuario '" + dto.getNombreUsuario() + "' ya está en uso");
        }

        // Permitir que la sucursal sea opcional (ej: para Admin central)
        Sucursal sucursal = null;
        if (dto.getSucursalId() != null) {
            sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + dto.getSucursalId()));
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
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // 1. Validar nombre de usuario si cambia
        if (!usuario.getNombreUsuarioUsuario().equals(dto.getNombreUsuario())) {
            if (usuarioRepository.existsByNombreUsuarioUsuario(dto.getNombreUsuario())) {
                throw new RuntimeException("El nuevo nombre de usuario ya está siendo usado por otra cuenta");
            }
            usuario.setNombreUsuarioUsuario(dto.getNombreUsuario());
        }

        // 2. Manejo de sucursal opcional
        if (dto.getSucursalId() != null) {
            Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
            usuario.setSucursal(sucursal);
        } else {
            usuario.setSucursal(null);
        }

        usuario.setUsuarioNombre(dto.getNombre());
        usuario.setUsuarioApellido(dto.getApellido());
        usuario.setUsuarioRol(dto.getRol());
        usuario.setUsuarioEstado(dto.getEstado());

        // 3. Actualización opcional de contraseña (si el DTO lo permite en el futuro)
        // if (dto.getNuevaContrasena() != null) usuario.setUsuarioContrasenaHash(passwordEncoder.encode(dto.getNuevaContrasena()));

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarUsuario(Long id){
        // En lugar de borrarlo físicamente, lo ideal es cambiar su estado para mantener integridad referencial
        cambiarEstado(id, false);
    }

    @Override
    public void cambiarEstado(Long id, Boolean nuevoEstado){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUsuarioEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id){
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioSimpleDTO> listarUsuariosActivos(){
        return usuarioRepository.findByUsuarioEstadoTrue()
                .stream()
                .map(UsuarioSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginDTO dto){
        Usuario usuario = usuarioRepository.findByNombreUsuarioUsuario(dto.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if(!passwordEncoder.matches(dto.getContrasena(), usuario.getUsuarioContrasenaHash())){
            throw new RuntimeException("Credenciales inválidas");
        }

        if(Boolean.FALSE.equals(usuario.getUsuarioEstado())){
            throw new RuntimeException("La cuenta de usuario está desactivada");
        }

        return UsuarioResponseDTO.fromEntity(usuario);
    }
}