package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.cajacorte.CajaCorteCreateDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteResponseDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteSimpleDTO;
import farmacias.AppOchoa.services.CajaCorteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cajacorte")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CajaCorteController {
    private final CajaCorteService cajaCorteService;

    @GetMapping
    public ResponseEntity<Page<CajaCorteSimpleDTO>> listarCortes(
            @PageableDefault(size = 10, sort = "corteTotalEfectivo")Pageable pageable){
        return ResponseEntity.ok(cajaCorteService.listarCortes(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CajaCorteResponseDTO> buscarPorId(@PathVariable Long id){
        CajaCorteResponseDTO cajaCorteResponseDTO = cajaCorteService.buscarPorId(id);
        return ResponseEntity.ok(cajaCorteResponseDTO);
    }
    @PostMapping
    public ResponseEntity<CajaCorteResponseDTO> crear(@Valid @RequestBody CajaCorteCreateDTO dto){
        CajaCorteResponseDTO cajaCorteResponseDTO = cajaCorteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaCorteResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        cajaCorteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
