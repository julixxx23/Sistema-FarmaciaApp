package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventafel.VentaFelResponseDTO;
import farmacias.AppOchoa.dto.ventafel.VentaFelSimpleDTO;
import farmacias.AppOchoa.services.VentaFelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
            @PageableDefault(size = 10, sort = "felNumeroAutorizacion")Pageable pageable){
        return ResponseEntity.ok(ventaFelService.listarActivas(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaFelResponseDTO> buscarPorId(@PathVariable Long id){
        VentaFelResponseDTO ventaFelResponseDTO = ventaFelService.buscarPorId(id);
        return ResponseEntity.ok(ventaFelResponseDTO);
    }


}
