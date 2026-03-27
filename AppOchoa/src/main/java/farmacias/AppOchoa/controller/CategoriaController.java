package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaUpdateDTO;
import farmacias.AppOchoa.services.CategoriaService;
import farmacias.AppOchoa.util.JwtUtil;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final JwtUtil jwtUtil;

    private Long getFarmaciaId(String authHeader){
        String token = authHeader.substring(7);
        return jwtUtil.extractFarmaciaId(token);
    }

    //Listar Paginacion de Categorias
    @GetMapping
    public ResponseEntity<Page<CategoriaSimpleDTO>> listarPaginados(
            @RequestHeader("Authorization") String authHeader,
            @PageableDefault(size = 10, sort = "categoriaNombre") Pageable pageable) {
        Page<CategoriaSimpleDTO> categorias = categoriaService.listarActivasPaginadas(getFarmaciaId(authHeader), pageable);
        return ResponseEntity.ok(categorias);
    }
    //Listar Categoria por Id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        CategoriaResponseDTO categoria = categoriaService.obtenerPorId(getFarmaciaId(authHeader),id);
        return ResponseEntity.ok(categoria);
    }

    //Método para crear
    @PostMapping
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<CategoriaResponseDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CategoriaCreateDTO dto){
        CategoriaResponseDTO nueva = categoriaService.crear(getFarmaciaId(authHeader),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    //Mètodo para actualizar completo
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @Valid @RequestBody CategoriaUpdateDTO dto){
        CategoriaResponseDTO actualizada = categoriaService.actualizar(getFarmaciaId(authHeader), id, dto);
        return ResponseEntity.ok(actualizada);
    }
    //Metodo para Actualizar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id, @RequestParam Boolean estado){
        categoriaService.cambiarEstado(getFarmaciaId(authHeader), id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('administrador')")
    public ResponseEntity<Void> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id){
        categoriaService.eliminar(getFarmaciaId(authHeader),id);
        return ResponseEntity.noContent().build();
    }


}