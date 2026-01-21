package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.usuario.LoginDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioCreateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioUpdateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioResponseDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto);

    UsuarioResponseDTO obtenerPorId(Long id);
    Page<UsuarioSimpleDTO> listarUsuariosActivosPaginado(Pageable pageable);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto);
    void cambiarEstado(Long id, Boolean nuevoEstado);
    void eliminarUsuario(Long id);

    UsuarioResponseDTO login(LoginDTO dto);
}