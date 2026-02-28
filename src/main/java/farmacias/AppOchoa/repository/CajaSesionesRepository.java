package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.SesionEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CajaSesionesRepository extends JpaRepository<CajaSesiones, Long> {

    // Buscar todas las sesiones de una caja
    List<CajaSesiones> findByCajaCajaId(Long cajaId);

    // Buscar todas las sesiones de un usuario
    List<CajaSesiones> findByUsuarioUsuarioId(Long usuarioId);

    // Buscar sesiones por estado
    List<CajaSesiones> findBySesionEstado(SesionEstado estado);

    // Buscar la sesión abierta de una caja
    Optional<CajaSesiones> findByCajaCajaIdAndSesionEstado(Long cajaId, SesionEstado estado);

    // Verificar si una caja tiene sesión abierta
    boolean existsByCajaCajaIdAndSesionEstado(Long cajaId, SesionEstado estado);
}