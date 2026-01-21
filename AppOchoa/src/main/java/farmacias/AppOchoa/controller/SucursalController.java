package farmacias.AppOchoa.controller;


import farmacias.AppOchoa.dto.sucursal.SucursalCreateDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalResponseDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.sucursal.SucursalUpdateDTO;
import farmacias.AppOchoa.services.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@CrossOrigin(origins= "*")
public class SucursalController {
    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<Page<SucursalSimpleDTO>> listarActivasPaginadas(
            @PageableDefault(size = 10, sort = "sucursalNombre")Pageable pageable){
                return ResponseEntity.ok(sucursalService.listarActivasPaginadas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerPorId(@PathVariable Long id){
        SucursalResponseDTO sucursalResponseDTO = sucursalService.obtenerPorId(id);
        return ResponseEntity.ok(sucursalResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<SucursalResponseDTO> crear(@Valid @RequestBody SucursalCreateDTO dto){
        SucursalResponseDTO sucursalResponseDTO = sucursalService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalUpdateDTO dto){
        SucursalResponseDTO sucursalResponseDTO = sucursalService.actualizar(id, dto);
        return ResponseEntity.ok(sucursalResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado){
        sucursalService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
