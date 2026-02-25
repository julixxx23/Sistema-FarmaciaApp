package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesCreateDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesResponseDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesSimpleDTO;
import farmacias.AppOchoa.services.CajaSesionesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cajasesiones")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CajaSesionesController {
    private final CajaSesionesService cajaSesionesService;

    @GetMapping
    public ResponseEntity<Page<CajaSesionesSimpleDTO>> listarSesiones(
            @PageableDefault(size = 10, sort = "sesionFechaApertura")Pageable pageable){
        return ResponseEntity.ok(cajaSesionesService.listarSesiones(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaSesionesResponseDTO> buscarPorId(@PathVariable Long id){
        CajaSesionesResponseDTO cajaSesionesResponseDTO = cajaSesionesService.buscarPorId(id);
        return ResponseEntity.ok(cajaSesionesResponseDTO);
    }
    @PostMapping
    public ResponseEntity<CajaSesionesResponseDTO> crear(@Valid @RequestBody CajaSesionesCreateDTO dto){
        CajaSesionesResponseDTO cajaSesionesResponseDTO = cajaSesionesService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaSesionesResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        cajaSesionesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
