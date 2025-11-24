package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
