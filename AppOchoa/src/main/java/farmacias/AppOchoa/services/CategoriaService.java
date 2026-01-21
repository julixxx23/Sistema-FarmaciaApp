package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.categoria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoriaService {
    // Métodos de escritura
    CategoriaResponseDTO crear(CategoriaCreateDTO dto);
    CategoriaResponseDTO actualizar(Long id, CategoriaUpdateDTO dto);
    void cambiarEstado(Long id, Boolean estado);
    void eliminar(Long id);

    // Métodos de consulta simple
    CategoriaResponseDTO obtenerPorId(Long id);
    List<CategoriaSimpleDTO> listarTodas();
    List<CategoriaSimpleDTO> listarActivas();

    // Métodos de paginación (El nombre que busca tu Controller)
    Page<CategoriaSimpleDTO> listarActivasPaginadas(Pageable pageable);
    Page<CategoriaSimpleDTO> listarTodasPaginadas(Pageable pageable);
}