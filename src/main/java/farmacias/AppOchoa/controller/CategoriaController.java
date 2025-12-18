package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaUpdateDTO;
import farmacias.AppOchoa.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    //Listar Paginacion de Categorias
    @GetMapping
    public ResponseEntity<Page<CategoriaSimpleDTO>> listarPaginados(
            @PageableDefault(size = 10, sort = "categoriaNombre") Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listarActivasPaginadas(pageable));
    }
    //Listar Categoria por Id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable Long id){
        CategoriaResponseDTO categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    //Método para crear
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaCreateDTO dto){
        CategoriaResponseDTO nueva = categoriaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    //Mètodo para actualizar completo
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaUpdateDTO dto){
        CategoriaResponseDTO actualizada = categoriaService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }
    //Metodo para Actualizar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado){
        categoriaService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}