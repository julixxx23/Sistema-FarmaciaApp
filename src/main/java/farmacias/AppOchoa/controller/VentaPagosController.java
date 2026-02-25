package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.ventapago.VentaPagoCreateDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoResponseDTO;
import farmacias.AppOchoa.dto.ventapago.VentaPagoSimpleDTO;
import farmacias.AppOchoa.services.VentaPagoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ventapagos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaPagosController {
    private final VentaPagoService ventaPagoService;

    @GetMapping
    public ResponseEntity<Page<VentaPagoSimpleDTO>> listarActivas(
            @PageableDefault(size = 10, sort = "auditoriaFechaCreacion")Pageable pageable){
        return ResponseEntity.ok(ventaPagoService.listarActivas(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaPagoResponseDTO> buscarPorId(@PathVariable Long id){
        VentaPagoResponseDTO ventaPagoResponseDTO = ventaPagoService.buscarPorId(id);
        return ResponseEntity.ok(ventaPagoResponseDTO);
    }
    @PostMapping
    public ResponseEntity<VentaPagoResponseDTO> crear(@Valid @RequestBody VentaPagoCreateDTO dto){
        VentaPagoResponseDTO ventaPagoResponseDTO = ventaPagoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaPagoResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ventaPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
