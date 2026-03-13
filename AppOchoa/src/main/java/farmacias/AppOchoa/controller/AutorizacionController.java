package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.autorizacion.AutorizacionCreateDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionResponseDTO;
import farmacias.AppOchoa.dto.autorizacion.AutorizacionSimpleDTO;
import farmacias.AppOchoa.services.AutorizacionService;
import farmacias.AppOchoa.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autorizaciones-controller")
public class AutorizacionController {
    private final AutorizacionService autorizacionService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<AutorizacionSimpleDTO>> listarTodas(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "autorizacionTipo")Pageable pageable){
        Page<AutorizacionSimpleDTO> autorizaciones = autorizacionService.listarTodas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(autorizaciones);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AutorizacionResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        AutorizacionResponseDTO autorizacionResponseDTO = autorizacionService.buscarPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(autorizacionResponseDTO);

    }
    @PostMapping
    public ResponseEntity<AutorizacionResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AutorizacionCreateDTO dto){
        AutorizacionResponseDTO autorizacionResponseDTO = autorizacionService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(autorizacionResponseDTO);
    }
    @GetMapping("/buscar")
    public ResponseEntity<Page<AutorizacionSimpleDTO>> buscar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            Pageable pageable) {
        return ResponseEntity.ok(autorizacionService.buscarPorTexto(getFarmaciaId(authHeader),texto, pageable));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        autorizacionService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }
}
