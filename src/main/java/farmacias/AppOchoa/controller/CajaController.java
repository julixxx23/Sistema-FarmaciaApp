package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.dto.caja.CajaUpdateDTO;
import farmacias.AppOchoa.model.CajaEstado;
import farmacias.AppOchoa.services.CajaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/caja")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CajaController {
    private final CajaService cajaService;

    @GetMapping
    public ResponseEntity<Page<CajaSimpleDTO>> listarCajasActivas(
            @PageableDefault(size = 10, sort = "cajaNombre")Pageable pageable){
        return  ResponseEntity.ok(cajaService.listarCajasActivas(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaResponseDTO> obtenerPorId(@PathVariable Long id){
        CajaResponseDTO cajaResponseDTO = cajaService.buscarPorId(id);
        return ResponseEntity.ok(cajaResponseDTO);
    }
    @PostMapping
    public ResponseEntity<CajaResponseDTO> crearCaja(@Valid @RequestBody CajaCreateDTO dto){
        CajaResponseDTO cajaResponseDTO = cajaService.crearCaja(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaResponseDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CajaResponseDTO> actualizarCaja(@PathVariable Long id, @Valid @RequestBody CajaUpdateDTO dto){
        CajaResponseDTO cajaResponseDTO = cajaService.actualizarCaja(id, dto);
        return ResponseEntity.ok(cajaResponseDTO);
    }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody CajaEstado cajaEstado){
        cajaService.cambiarEstado(id, cajaEstado);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        cajaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
