package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Farmacia;
import farmacias.AppOchoa.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryFarmacia extends JpaRepository<Farmacia, Long> {
    Page<Producto> findByFarmacia_FarmaciaIdAndProductoEstadoTrue(
            Long farmaciaId, Pageable pageable);

}
