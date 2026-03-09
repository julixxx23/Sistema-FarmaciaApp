package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.dto.compra.CompraSimpleDTO;
import farmacias.AppOchoa.dto.compra.CompraUpdateDTO;
import farmacias.AppOchoa.model.CompraEstado;
import farmacias.AppOchoa.services.CompraService;
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
@RequestMapping("/api/v1/compras")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CompraController {
    private final CompraService compraService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }
    @GetMapping
    public ResponseEntity<Page<CompraSimpleDTO>> listarActivasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "compraFecha")Pageable pageable){
        Page<CompraSimpleDTO> compras = compraService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> listarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        CompraResponseDTO compraResponseDTO = compraService.listarPorId(getFarmaciaId(authHeader), id);
        return ResponseEntity.ok(compraResponseDTO);
    }

    @GetMapping("/todas")
    public ResponseEntity<Page<CompraSimpleDTO>> listarTodasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "compraFecha")Pageable pageable){
        Page<CompraSimpleDTO> compras1 = compraService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(compras1);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<CompraResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CompraCreateDTO dto){
        CompraResponseDTO compraResponseDTO = compraService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody CompraUpdateDTO dto){
        CompraResponseDTO compraResponseDTO = compraService.actualizar(getFarmaciaId(authHeader),id, dto);
        return ResponseEntity.ok(compraResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @RequestParam CompraEstado compraEstado){
        compraService.cambiarEstado(getFarmaciaId(authHeader),id, compraEstado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        compraService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }


}
