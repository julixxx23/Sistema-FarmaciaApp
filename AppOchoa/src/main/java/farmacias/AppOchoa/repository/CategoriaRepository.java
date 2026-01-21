package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByCategoriaEstadoTrue();
    // Este método debe recibir Pageable para que el Service no marque error
    Page<Categoria> findByCategoriaEstadoTrue(Pageable pageable);
    boolean existsByCategoriaNombre(String nombre);
}