package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoSimpleDTO;
import farmacias.AppOchoa.services.VentaFelNotasCreditoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ventasnotascreditos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaFelNotasCreditoController {
    private final VentaFelNotasCreditoService ventaFelNotasCreditoService;


    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaFelNotasCreditoSimpleDTO>> buscar(
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(ventaFelNotasCreditoService.buscarPorTexto(texto, pageable));
    }
}
