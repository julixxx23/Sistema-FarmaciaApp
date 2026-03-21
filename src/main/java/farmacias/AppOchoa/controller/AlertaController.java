package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.alerta.AlertaCreateDTO;
import farmacias.AppOchoa.dto.alerta.AlertaResponseDTO;
import farmacias.AppOchoa.dto.alerta.AlertaSimpleDTO;
import farmacias.AppOchoa.dto.alerta.AlertaUpdateDTO;
import farmacias.AppOchoa.services.AlertaService;
import farmacias.AppOchoa.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
@AllArgsConstructor
@CrossOrigin(origins = "*") // URL Despegada
public class AlertaController {
    private final AlertaService alertaService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token  = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<AlertaSimpleDTO>> listarTodasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "alertaFecha", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<AlertaSimpleDTO> alerta = alertaService.listarTodasPaginadas(getFarmaciaId(authHeader),pageable);
        return ResponseEntity.ok(alerta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> listarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        AlertaResponseDTO alertaResponseDTO = alertaService.listarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(alertaResponseDTO);
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Page<AlertaSimpleDTO>> listarNoLeidasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "alertaFecha", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<AlertaSimpleDTO> alertas = alertaService.listarNoLeidasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(alertas);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<AlertaResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AlertaCreateDTO dto) {
        AlertaResponseDTO alertaResponseDTO = alertaService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody AlertaUpdateDTO dto) {
        AlertaResponseDTO alertaResponseDTO = alertaService.actualizar(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(alertaResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        alertaService.cambiarEstado(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        alertaService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}