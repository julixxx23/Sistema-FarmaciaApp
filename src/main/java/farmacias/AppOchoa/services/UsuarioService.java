package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.usuario.LoginDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioCreateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioUpdateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioResponseDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioSimpleDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO dto);

    void eliminarUsuario(Long id);
    void cambiarEstado(Long id, Boolean nuevoEstado);

    UsuarioResponseDTO obtenerPorId(Long id);
    List<UsuarioSimpleDTO> listarUsuariosActivos();
    UsuarioResponseDTO login(LoginDTO dto);
}
