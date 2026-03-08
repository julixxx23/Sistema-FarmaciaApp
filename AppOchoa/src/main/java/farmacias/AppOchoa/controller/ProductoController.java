package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.producto.ProductoCreateDTO;
import farmacias.AppOchoa.dto.producto.ProductoResponseDTO;
import farmacias.AppOchoa.dto.producto.ProductoSimpleDTO;
import farmacias.AppOchoa.dto.producto.ProductoUpdateDTO;
import farmacias.AppOchoa.services.ProductoService;
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
@RequestMapping("/api/v1/productos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<ProductoSimpleDTO>> listarProductosActivos(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "productoNombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarProductosActivos(getFarmaciaId(authHeader), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(getFarmaciaId(authHeader), id));
    }

    @GetMapping("/codigobarras")
    public ResponseEntity<ProductoResponseDTO> obtenerPorCodigoBarras(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String codigo) {
        return ResponseEntity.ok(productoService.obtenerPorCodigoBarras(getFarmaciaId(authHeader), codigo));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> agregarProducto(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ProductoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.agregarProducto(getFarmaciaId(authHeader), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(getFarmaciaId(authHeader), id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam Boolean estado) {
        productoService.cambiarEstado(getFarmaciaId(authHeader), id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminarProducto(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        productoService.eliminarProducto(getFarmaciaId(authHeader), id);
        return ResponseEntity.noContent().build();
    }
}