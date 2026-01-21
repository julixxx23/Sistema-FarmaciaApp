package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import farmacias.AppOchoa.model.LoteEstado;
import farmacias.AppOchoa.services.InventarioLotesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/inventarioslotes")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class InventarioLotesController {
    private final InventarioLotesService inventarioLotesService;

    @GetMapping
    public ResponseEntity<Page<InventarioLotesSimpleDTO>> listarPorSucursalPaginado(
            @RequestParam Long sucursalId, @PageableDefault(size = 10, sort = "loteCantidadActual")Pageable pageable){
        return ResponseEntity.ok(inventarioLotesService.listarPorSucursalPaginado(sucursalId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioLotesResponseDTO> buscarPorId(@PathVariable Long id){
        InventarioLotesResponseDTO inventarioLotesResponseDTO = inventarioLotesService.buscarPorId(id);
        return ResponseEntity.ok(inventarioLotesResponseDTO);
    }

    @GetMapping("/proximos-vencer")
    public ResponseEntity<Page<InventarioLotesSimpleDTO>> listarProximosAVencerPaginado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLimite,
            @PageableDefault(size = 10, sort = "loteFechaVencimiento") Pageable pageable) {
        return ResponseEntity.ok(inventarioLotesService.listarProximosAVencerPaginado(fechaLimite, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<InventarioLotesResponseDTO> crear(@Valid @RequestBody InventarioLotesCreateDTO dto){
        InventarioLotesResponseDTO inventarioLotesResponseDTO = inventarioLotesService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioLotesResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioLotesResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioLotesUpdateDTO dto){
        InventarioLotesResponseDTO inventarioLotesResponseDTO = inventarioLotesService.actualizar(id, dto);
            return ResponseEntity.ok(inventarioLotesResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        inventarioLotesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }





}
