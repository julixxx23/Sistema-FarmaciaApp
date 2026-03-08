package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import farmacias.AppOchoa.services.SucursalService;
import farmacias.AppOchoa.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SucursalController {

    private final SucursalService sucursalService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<SucursalSimpleDTO>> listarActivasPaginadas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "sucursalNombre") Pageable pageable) {
        Page<SucursalSimpleDTO> sucursales = sucursalService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        SucursalResponseDTO sucursalResponseDTO = sucursalService.obtenerPorId(getFarmaciaId(authHeader), id);
        return ResponseEntity.ok(sucursalResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<SucursalResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SucursalCreateDTO dto) {
        SucursalResponseDTO sucursalResponseDTO = sucursalService.crear(getFarmaciaId(authHeader), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody SucursalUpdateDTO dto) {
        SucursalResponseDTO sucursalResponseDTO = sucursalService.actualizar(getFarmaciaId(authHeader), id, dto);
        return ResponseEntity.ok(sucursalResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam Boolean estado) {
        sucursalService.cambiarEstado(getFarmaciaId(authHeader), id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        sucursalService.eliminar(getFarmaciaId(authHeader), id);
        return ResponseEntity.noContent().build();
    }
}