package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventario.InventarioUpdateDTO;
import farmacias.AppOchoa.services.InventarioService;
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
@RequestMapping("/api/v1/inventarios")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class InventarioController {
    private final InventarioService inventarioService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<InventarioSimpleDTO>> listarActivosPaginado(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "inventarioCantidadActual")Pageable pageable){
        Page<InventarioSimpleDTO> inventarios = inventarioService.listarActivosPaginado(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> listarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.listaPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(inventarioResponseDTO);
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<InventarioSimpleDTO>> listarTodosPaginados(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "inventarioCantidadActual")Pageable pageable){
        Page<InventarioSimpleDTO> inventarios1 = inventarioService.listarTodosPaginado(getFarmaciaId(authHeader), pageable);

        return ResponseEntity.ok(inventarios1);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<InventarioResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody InventarioCreateDTO dto){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody InventarioUpdateDTO dto){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.actualizar(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(inventarioResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        inventarioService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }



}
