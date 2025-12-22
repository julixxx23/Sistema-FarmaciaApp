package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventario.InventarioUpdateDTO;
import farmacias.AppOchoa.services.InventarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventarios")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class InventarioController {
    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<Page<InventarioSimpleDTO>> listarActivosPaginado(
            @PageableDefault(size = 10, sort = "inventarioCantidadActual")Pageable pageable){
        return ResponseEntity.ok(inventarioService.listarActivosPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> listarPorId(@PathVariable Long id){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.listaPorId(id);
        return ResponseEntity.ok(inventarioResponseDTO);
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<InventarioSimpleDTO>> listarTodosPaginados(
            @PageableDefault(size = 10, sort = "inventarioCantidadActual")Pageable pageable){
        return ResponseEntity.ok(inventarioService.listarTodosPaginado(pageable));
    }

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioCreateDTO dto){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioUpdateDTO dto){
        InventarioResponseDTO inventarioResponseDTO = inventarioService.actualizar(id, dto);
        return ResponseEntity.ok(inventarioResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }



}
