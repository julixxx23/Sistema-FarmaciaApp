package farmacias.AppOchoa.controller;


import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.dto.venta.VentaSimpleDTO;
import farmacias.AppOchoa.dto.venta.VentaUpdateDTO;
import farmacias.AppOchoa.model.VentaEstado;
import farmacias.AppOchoa.services.VentaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ventas")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {
    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<Page<VentaSimpleDTO>> listarActivasPaginadas(
            @PageableDefault(size = 10, sort = "ventaFecha")Pageable pageable){
        return ResponseEntity.ok(ventaService.listarActivasPaginadas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> listarPorId(@PathVariable Long id){
        VentaResponseDTO ventaResponseDTO = ventaService.listarPorId(id);
        return ResponseEntity.ok(ventaResponseDTO);
    }

    @GetMapping("/todas")
    public ResponseEntity<Page<VentaSimpleDTO>> listarTodasPaginadas(
            @PageableDefault(size = 10, sort = "ventaFecha")Pageable pageable){
        return ResponseEntity.ok(ventaService.listarTodasPaginadas(pageable));
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(@Valid @RequestBody VentaCreateDTO dto){
        VentaResponseDTO ventaResponseDTO = ventaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody VentaUpdateDTO dto){
        VentaResponseDTO ventaResponseDTO = ventaService.actualizar(id, dto);
        return ResponseEntity.ok(ventaResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody VentaEstado ventaEstado){
        ventaService.cambiarEstado(id, ventaEstado );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }



}
