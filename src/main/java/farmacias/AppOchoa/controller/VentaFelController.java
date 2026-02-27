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
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "felNumeroAutorizacion") Pageable pageable) {

        //Si el usuario escribió algo en la barra de búsqueda
        if (buscar != null && !buscar.isBlank()) {
            return ResponseEntity.ok(ventaFelService.buscarPorTermino(buscar, pageable));
        }

        //Si la barra está vacía, lista todas las facturas
        return ResponseEntity.ok(ventaFelService.listarActivas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaFelResponseDTO> buscarPorId(@PathVariable Long id) {
        VentaFelResponseDTO ventaFelResponseDTO = ventaFelService.buscarPorId(id);
        return ResponseEntity.ok(ventaFelResponseDTO);
    }

    @PostMapping
    public ResponseEntity<VentaFelResponseDTO> crear(@Valid @RequestBody VentaFelCreateDTO dto) {
        VentaFelResponseDTO ventaFelResponseDTO = ventaFelService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaFelResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaFelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}