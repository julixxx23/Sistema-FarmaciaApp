package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionSimpleDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionUpdateDTO;
import farmacias.AppOchoa.services.PresentacionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/presentaciones")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PresentacionController {
    private final PresentacionService presentacionService;

    @GetMapping
    public ResponseEntity<Page<PresentacionSimpleDTO>> listarActivasPaginadas(
            @PageableDefault(size = 10, sort = "presentacionNombre")Pageable pageable){
        return ResponseEntity.ok(presentacionService.listarActivasPaginadas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentacionResponseDTO> obtenerPorId(@PathVariable Long id){
        PresentacionResponseDTO presentacionResponseDTO = presentacionService.obtenerPorId(id);
        return ResponseEntity.ok(presentacionResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<PresentacionResponseDTO> crear(@Valid @RequestBody PresentacionCreateDTO dto){
        PresentacionResponseDTO presentacionResponseDTO = presentacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(presentacionResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresentacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PresentacionUpdateDTO dto){
        PresentacionResponseDTO presentacionResponseDTO = presentacionService.actualizar(id, dto);
        return ResponseEntity.ok(presentacionResponseDTO);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado){
        presentacionService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        presentacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }






}
