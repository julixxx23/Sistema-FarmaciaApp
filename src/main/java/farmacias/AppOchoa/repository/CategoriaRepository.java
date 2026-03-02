package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByCategoriaEstadoTrue();
    Page<Categoria> findByCategoriaEstadoTrue(Pageable pageable);
    boolean existsByCategoriaNombre(String nombre);
    @Query("SELECT c FROM Categoria c WHERE " +
            "LOWER(c.categoriaNombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Categoria> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}