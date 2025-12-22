package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.dto.compra.CompraSimpleDTO;
import farmacias.AppOchoa.dto.compra.CompraUpdateDTO;
import farmacias.AppOchoa.model.CompraEstado;
import farmacias.AppOchoa.services.CompraService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compras")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CompraController {
    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<Page<CompraSimpleDTO>> listarActivasPaginadas(
            @PageableDefault(size = 10, sort = "compraFecha")Pageable pageable){
        return ResponseEntity.ok(compraService.listarActivasPaginadas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> listarPorId(@PathVariable Long id){
        CompraResponseDTO compraResponseDTO = compraService.listarPorId(id);
        return ResponseEntity.ok(compraResponseDTO);
    }

    @GetMapping("/todas")
    public ResponseEntity<Page<CompraSimpleDTO>> listarTodasPaginadas(
            @PageableDefault(size = 10, sort = "compraFecha")Pageable pageable){
        return ResponseEntity.ok(compraService.listarTodasPaginadas(pageable));
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> crear(@Valid @RequestBody CompraCreateDTO dto){
        CompraResponseDTO compraResponseDTO = compraService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CompraUpdateDTO dto){
        CompraResponseDTO compraResponseDTO = compraService.actualizar(id, dto);
        return ResponseEntity.ok(compraResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam CompraEstado compraEstado){
        compraService.cambiarEstado(id, compraEstado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        compraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
