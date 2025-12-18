package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<CategoriaSimpleDTO>> listarPaginados(
            @PageableDefault(size = 10, sort = "categoriaNombre") Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listarActivasPaginadas(pageable));
    }
}