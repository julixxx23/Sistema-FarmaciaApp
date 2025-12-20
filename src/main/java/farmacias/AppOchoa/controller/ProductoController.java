package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.dto.producto.ProductoCreateDTO;
import farmacias.AppOchoa.dto.producto.ProductoResponseDTO;
import farmacias.AppOchoa.dto.producto.ProductoSimpleDTO;
import farmacias.AppOchoa.dto.producto.ProductoUpdateDTO;
import farmacias.AppOchoa.services.ProductoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoSimpleDTO>> listarProductosActivos(
            @PageableDefault(size = 10, sort = "productoNombre")Pageable pageable){
        return ResponseEntity.ok(productoService.listarProductosActivos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id){
        ProductoResponseDTO productoResponseDTO = productoService.obtenerPorId(id);
        return ResponseEntity.ok(productoResponseDTO);
    }

    @GetMapping("/codigobarras")
    public ResponseEntity<ProductoResponseDTO> obtenerPorCodigoBarras(@RequestParam String codigo){
        ProductoResponseDTO productoResponseDTO = productoService.obtenerPorCodigoBarras(codigo);
        return ResponseEntity.ok(productoResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> agregarProducto(@Valid @RequestBody ProductoCreateDTO dto){
        ProductoResponseDTO productoResponseDTO = productoService.agregarProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoUpdateDTO dto){
        ProductoResponseDTO productoResponseDTO = productoService.actualizarProducto(id, dto);
        return ResponseEntity.ok(productoResponseDTO);
    }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado){
        productoService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


}
