package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.suscripcionpago.SuscripcionPagoCreateDTO;
import farmacias.AppOchoa.dto.suscripcionpago.SuscripcionPagoResponseDTO;
import farmacias.AppOchoa.dto.suscripcionpago.SuscripcionPagoSimpleDTO;
import farmacias.AppOchoa.services.SuscripcionPagoService;
import farmacias.AppOchoa.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suscripciones")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SuscripcionPagoController {
    private final SuscripcionPagoService suscripcionPagoService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    @GetMapping
    public ResponseEntity<Page<SuscripcionPagoSimpleDTO>> listarSuscripciones(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "pagoPlan")Pageable pageable){
        Page<SuscripcionPagoSimpleDTO> suscripcionPagoSimpleDTOS = suscripcionPagoService.listarSuscripciones(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(suscripcionPagoSimpleDTOS);
    }
    @GetMapping("/buscar/{id}")
    public ResponseEntity<SuscripcionPagoResponseDTO> buscarPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        SuscripcionPagoResponseDTO suscripcionPagoResponseDTO = suscripcionPagoService.buscarPorId(getFarmaciaId(authHeader), id);
        return ResponseEntity.ok(suscripcionPagoResponseDTO);
    }
    @GetMapping("/buscar/texto")
    public ResponseEntity<Page<SuscripcionPagoSimpleDTO>> buscarPorTexto(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String texto,
            @PageableDefault(size = 10, sort = "pagoPlan") Pageable pageable){
        Page<SuscripcionPagoSimpleDTO> suscripcionPagos = suscripcionPagoService.buscarPorTexto(getFarmaciaId(authHeader), texto, pageable);
        return ResponseEntity.ok(suscripcionPagos);
    }
    @PostMapping
    public ResponseEntity<SuscripcionPagoResponseDTO> crear(
            @RequestHeader("Authorization")String authHeader,
            @Valid @RequestBody SuscripcionPagoCreateDTO dto){
        SuscripcionPagoResponseDTO suscripcionPagoResponseDTO = suscripcionPagoService.crear(getFarmaciaId(authHeader), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionPagoResponseDTO);
    }
    @DeleteMapping("/buscar/{id})")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        suscripcionPagoService.eliminar(getFarmaciaId(authHeader), id);
        return ResponseEntity.noContent().build();
    }




}
