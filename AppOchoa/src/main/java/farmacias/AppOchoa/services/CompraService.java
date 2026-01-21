package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.dto.compra.CompraSimpleDTO;
import farmacias.AppOchoa.dto.compra.CompraUpdateDTO;
import farmacias.AppOchoa.model.CompraEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompraService {

    CompraResponseDTO crear(CompraCreateDTO dto);

    CompraResponseDTO listarPorId(Long id);
    Page<CompraSimpleDTO> listarTodasPaginadas(Pageable pageable);
    Page<CompraSimpleDTO> listarActivasPaginadas(Pageable pageable);

    CompraResponseDTO actualizar(Long id, CompraUpdateDTO dto);
    void cambiarEstado(Long id, CompraEstado compraEstado);
    void eliminar(Long id);
}