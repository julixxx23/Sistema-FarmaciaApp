package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.cajacorte.CajaCorteCreateDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteResponseDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteSimpleDTO;
import farmacias.AppOchoa.services.CajaCorteService;
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
@RequestMapping("/api/v1/cajacorte")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Caja Corte-Controller")
public class CajaCorteController {
    private final CajaCorteService cajaCorteService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<CajaCorteSimpleDTO>> listarCortes(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "corteTotalEfectivo")Pageable pageable){
        Page<CajaCorteSimpleDTO> cajascortes = cajaCorteService.listarCortes(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(cajascortes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaCorteResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        CajaCorteResponseDTO cajaCorteResponseDTO = cajaCorteService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(cajaCorteResponseDTO);
    }
    @GetMapping("/buscar")
    public ResponseEntity<Page<CajaCorteSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(cajaCorteService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @PostMapping
    public ResponseEntity<CajaCorteResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CajaCorteCreateDTO dto){
        CajaCorteResponseDTO cajaCorteResponseDTO = cajaCorteService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaCorteResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        cajaCorteService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}
