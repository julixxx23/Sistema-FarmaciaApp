package farmacias.AppOchoa.repository;

import farmacias.AppOchoa.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    // 1. Validar nombre único
    boolean existsBySucursalNombre(String nombre);

    // 2. Listar solo sucursales activas
    List<Sucursal> findBySucursalEstadoTrue();

    // 3. Buscar por nombre
    List<Sucursal> findBySucursalNombreContainingIgnoreCase(String nombre);
}