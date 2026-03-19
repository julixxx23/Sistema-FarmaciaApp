package farmacias.AppOchoa.controller;


import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.dto.venta.VentaSimpleDTO;
import farmacias.AppOchoa.dto.venta.VentaUpdateDTO;
import farmacias.AppOchoa.model.VentaEstado;
import farmacias.AppOchoa.services.VentaService;
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

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ventas")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {
    private final VentaService ventaService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);

    }

    @GetMapping
    public ResponseEntity<Page<VentaSimpleDTO>> listarActivasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "ventaFecha")Pageable pageable){
        Page<VentaSimpleDTO> ventas = ventaService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> listarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        VentaResponseDTO ventaResponseDTO = ventaService.listarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(ventaResponseDTO);
    }

    @GetMapping("/todas")
    public ResponseEntity<Page<VentaSimpleDTO>> listarTodasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "ventaFecha")Pageable pageable){
        Page<VentaSimpleDTO> ventas1 = ventaService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(ventas1);
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody VentaCreateDTO dto){
        VentaResponseDTO ventaResponseDTO = ventaService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody VentaUpdateDTO dto){
        VentaResponseDTO ventaResponseDTO = ventaService.actualizar(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(ventaResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @RequestBody VentaEstado ventaEstado){
        ventaService.cambiarEstado(getFarmaciaId(authHeader),id, ventaEstado );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        ventaService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }



}
