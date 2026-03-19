package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoCreateDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoResponseDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoSimpleDTO;
import farmacias.AppOchoa.services.VentaFelNotasCreditoService;
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
@RequestMapping("/api/v1/ventasnotascreditos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Ventas Fel Notas Credito-controller")
public class VentaFelNotasCreditoController {
    private final VentaFelNotasCreditoService ventaFelNotasCreditoService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }


    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaFelNotasCreditoSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(ventaFelNotasCreditoService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaFelNotasCreditoResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        VentaFelNotasCreditoResponseDTO ventaFelNotasCreditoResponseDTO = ventaFelNotasCreditoService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(ventaFelNotasCreditoResponseDTO);
    }
    @GetMapping
    public ResponseEntity<Page<VentaFelNotasCreditoSimpleDTO>> listarNotas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "notaEstado")Pageable pageable){
        return ResponseEntity.ok(ventaFelNotasCreditoService.listarNotas(getFarmaciaId(authHeader),pageable));
    }
    @PostMapping
    public ResponseEntity<VentaFelNotasCreditoResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody VentaFelNotasCreditoCreateDTO dto){
        VentaFelNotasCreditoResponseDTO ventaFelNotasCreditoResponseDTO = ventaFelNotasCreditoService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaFelNotasCreditoResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        ventaFelNotasCreditoService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }

}
