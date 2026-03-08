package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.usuario.*;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.services.UsuarioService;
import farmacias.AppOchoa.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioSimpleDTO>> listarUsuarioPaginados(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "usuarioNombre")Pageable pageable){
        Page<UsuarioSimpleDTO> usuarios = usuarioService.listarUsuariosActivosPaginado(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.obtenerPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UsuarioCreateDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.crearUsuario(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.actualizarUsuario(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @RequestParam Boolean estado){
        usuarioService.cambiarEstado(getFarmaciaId(authHeader),id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        usuarioService.eliminarUsuario(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
    //Login
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LoginDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.login(getFarmaciaId(authHeader),dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }




}
