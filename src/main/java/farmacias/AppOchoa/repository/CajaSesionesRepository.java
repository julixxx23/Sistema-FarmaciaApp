package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesCreateDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesResponseDTO;
import farmacias.AppOchoa.dto.cajasesiones.CajaSesionesSimpleDTO;
import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.SesionEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CajaSesionesRepository extends JpaRepository<CajaSesiones, Long> {

    //Buscar todas las sesiones de una caja
    List<CajaSesiones> findByCajaId(Long cajaId);

    //Buscar todas las sesiones de un usuario (cajero)
    List<CajaSesiones> findByUsuarioId(Long usuarioId);

    //Buscar sesiones por estado
    List<CajaSesiones> findBySesionEstado(SesionEstado estado);

    //Buscar la sesión abierta de una caja (crítico - solo puede haber una)
    Optional<CajaSesiones> findByCajaIdAndSesionEstado(Long cajaId, SesionEstado estado);

    //Verificar si una caja tiene sesión abierta (validación antes de abrir)
    boolean existsByCajaIdAndSesionEstado(Long cajaId, SesionEstado estado);

    CajaSesionesResponseDTO crear(CajaSesionesCreateDTO dto);

    @Transactional(readOnly = true)
    CajaSesionesResponseDTO buscarPorId(Long id);

    @Transactional(readOnly = true)
    Page<CajaSesionesSimpleDTO> listarSesiones(Pageable pageable);

    void eliminar(Long id);
}