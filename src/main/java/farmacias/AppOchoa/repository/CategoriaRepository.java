package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    //Solo validación de unicidad
    boolean existsByCategoriaNombre(String nombre);

    // Listado de activas
    List<Categoria> findByCategoriaEstadoTrue();
}