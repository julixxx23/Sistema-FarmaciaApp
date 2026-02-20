package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CajaRepository extends JpaRepository<Caja, Long> {
    boolean existsByCajaNombre(String caja);
    List<Caja> findByCajaId(Long id);
    boolean existsBySucursalIdAndCajaNombre(Long sucursalId, String cajaNombre);

}
