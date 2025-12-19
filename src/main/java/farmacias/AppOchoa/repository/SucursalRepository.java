package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Sucursal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    // Validar nombre único
    boolean existsBySucursalNombre(String nombre);

    // Listar solo sucursales activas (sin paginación)
    List<Sucursal> findBySucursalEstadoTrue();

    // Listar solo sucursales activas CON paginación
    Page<Sucursal> findBySucursalEstadoTrue(Pageable pageable);

    // Buscar por nombre (sin paginación)
    List<Sucursal> findBySucursalNombreContainingIgnoreCase(String nombre);

    // Buscar por nombre CON paginación
    Page<Sucursal> findBySucursalNombreContainingIgnoreCase(String nombre, Pageable pageable);
}