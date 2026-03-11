package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventapago.VentaPagoCreateDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoResponseDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoSimpleDTO;
import farmacias.AppOchoa.services.VentaPagoService;
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
@RequestMapping("/api/v1/ventapagos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Ventas pagos-controller")
public class VentaPagosController {
    private final VentaPagoService ventaPagoService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<VentaPagoSimpleDTO>> listarActivas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "auditoriaFechaCreacion")Pageable pageable){
        Page<VentaPagoSimpleDTO> ventas = ventaPagoService.listarActivas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(ventas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaPagoResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        VentaPagoResponseDTO ventaPagoResponseDTO = ventaPagoService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(ventaPagoResponseDTO);
    }
    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaPagoSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(ventaPagoService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @PostMapping
    public ResponseEntity<VentaPagoResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody VentaPagoCreateDTO dto){
        VentaPagoResponseDTO ventaPagoResponseDTO = ventaPagoService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaPagoResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        ventaPagoService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}
