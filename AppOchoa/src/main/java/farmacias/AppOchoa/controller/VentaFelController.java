package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventafel.VentaFelCreateDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelResponseDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelSimpleDTO;
import farmacias.AppOchoa.services.VentaFelService;
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
@RequestMapping("/api/v1/ventasfel")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Ventas Fel-controller")
public class VentaFelController {

    private final VentaFelService ventaFelService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<VentaFelSimpleDTO>> listarActivas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "felNumeroAutorizacion") Pageable pageable) {
        Page<VentaFelSimpleDTO> ventasfel = ventaFelService.listarActivas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(ventasfel);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaFelSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(ventaFelService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaFelResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(ventaFelService.buscarPorId(getFarmaciaId(authHeader),id));
    }

    @PostMapping
    public ResponseEntity<VentaFelResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody VentaFelCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaFelService.crear(getFarmaciaId(authHeader),dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        ventaFelService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}