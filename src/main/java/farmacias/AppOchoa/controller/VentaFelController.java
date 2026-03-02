package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventafel.VentaFelCreateDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelResponseDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelSimpleDTO;
import farmacias.AppOchoa.services.VentaFelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ventasfel")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaFelController {

    private final VentaFelService ventaFelService;

    @GetMapping
    public ResponseEntity<Page<VentaFelSimpleDTO>> listarActivas(
            @PageableDefault(size = 10, sort = "felNumeroAutorizacion") Pageable pageable) {
        return ResponseEntity.ok(ventaFelService.listarActivas(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaFelSimpleDTO>> buscar(
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(ventaFelService.buscarPorTexto(texto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaFelResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaFelService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentaFelResponseDTO> crear(@Valid @RequestBody VentaFelCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaFelService.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaFelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}