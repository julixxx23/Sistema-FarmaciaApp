package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesCreateDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesResponseDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesSimpleDTO;
import farmacias.AppOchoa.services.CajaSesionesService;
import farmacias.AppOchoa.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cajasesiones")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "CajaSesiones-controller")
public class CajaSesionesController {
    private final CajaSesionesService cajaSesionesService;
    private JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<CajaSesionesSimpleDTO>> listarSesiones(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "sesionFechaApertura")Pageable pageable){
        Page<CajaSesionesSimpleDTO> cajasesiones = cajaSesionesService.listarSesiones(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(cajasesiones);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaSesionesResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        CajaSesionesResponseDTO cajaSesionesResponseDTO = cajaSesionesService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(cajaSesionesResponseDTO);
    }
    @GetMapping("/buscar")
    public ResponseEntity<Page<CajaSesionesSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(cajaSesionesService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @PostMapping
    public ResponseEntity<CajaSesionesResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CajaSesionesCreateDTO dto){
        CajaSesionesResponseDTO cajaSesionesResponseDTO = cajaSesionesService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaSesionesResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        cajaSesionesService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}
