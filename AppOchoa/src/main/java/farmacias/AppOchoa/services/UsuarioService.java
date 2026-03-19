package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.usuario.LoginDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioCreateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioUpdateDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioResponseDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(Long farmaciaId, UsuarioCreateDTO dto);
    UsuarioResponseDTO obtenerPorId(Long farmaciaId, Long id);
    Page<UsuarioSimpleDTO> listarUsuariosActivosPaginado(Long farmaciaId, Pageable pageable);
    UsuarioResponseDTO actualizarUsuario(Long farmaciaId, Long id, UsuarioUpdateDTO dto);
    void cambiarEstado(Long farmaciaId, Long id, Boolean nuevoEstado);
    void eliminarUsuario(Long farmaciaId, Long id);
    UsuarioResponseDTO login(Long farmaciaId, LoginDTO dto);
}