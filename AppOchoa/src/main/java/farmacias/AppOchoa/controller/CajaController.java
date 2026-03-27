package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.dto.caja.CajaUpdateDTO;
import farmacias.AppOchoa.model.CajaEstado;
import farmacias.AppOchoa.services.CajaService;
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
@RequestMapping("/api/v1/caja")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Caja-controller")
public class CajaController {
    private final CajaService cajaService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<CajaSimpleDTO>> listarCajasActivas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "cajaNombre")Pageable pageable){
        Page<CajaSimpleDTO> cajas = cajaService.listarCajasActivas(getFarmaciaId(authHeader), pageable);
        return  ResponseEntity.ok(cajas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaResponseDTO> obtenerPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        CajaResponseDTO cajaResponseDTO = cajaService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(cajaResponseDTO);
    }
    @GetMapping("/buscar")
    public ResponseEntity<Page<CajaSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(cajaService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @PostMapping
    public ResponseEntity<CajaResponseDTO> crearCaja(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CajaCreateDTO dto){
        CajaResponseDTO cajaResponseDTO = cajaService.crearCaja(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaResponseDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CajaResponseDTO> actualizarCaja(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody CajaUpdateDTO dto){
        CajaResponseDTO cajaResponseDTO = cajaService.actualizarCaja(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(cajaResponseDTO);
    }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @RequestBody CajaEstado cajaEstado){
        cajaService.cambiarEstado(getFarmaciaId(authHeader),id, cajaEstado);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        cajaService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}
