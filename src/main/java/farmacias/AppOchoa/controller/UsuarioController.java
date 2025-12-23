package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.usuario.*;
import farmacias.AppOchoa.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioSimpleDTO>> listarUsuarioPaginados(
            @PageableDefault(size = 10, sort = "usuarioNombre")Pageable pageable){
        return ResponseEntity.ok(usuarioService.listarUsuariosActivosPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado){
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    //Login
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody LoginDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.login(dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }




}
