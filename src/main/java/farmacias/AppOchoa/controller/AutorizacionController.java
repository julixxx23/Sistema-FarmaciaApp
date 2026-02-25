package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.autorizacion.AutorizacionCreateDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionResponseDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionSimpleDTO;
import farmacias.AppOchoa.services.AutorizacionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/autorizaciones")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AutorizacionController {
    private final AutorizacionService autorizacionService;

    @GetMapping
    public ResponseEntity<Page<AutorizacionSimpleDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "autorizacionTipo")Pageable pageable){
        return ResponseEntity.ok(autorizacionService.listarTodas(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AutorizacionResponseDTO> buscarPorId(@PathVariable Long id){
        AutorizacionResponseDTO autorizacionResponseDTO = autorizacionService.buscarPorId(id);
        return ResponseEntity.ok(autorizacionResponseDTO);

    }
    @PostMapping
    public ResponseEntity<AutorizacionResponseDTO> crear(@Valid @RequestBody AutorizacionCreateDTO dto){
        AutorizacionResponseDTO autorizacionResponseDTO = autorizacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorizacionResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        autorizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
