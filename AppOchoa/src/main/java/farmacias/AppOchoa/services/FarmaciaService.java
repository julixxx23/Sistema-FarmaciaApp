package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.farmacia.FarmaciaCreateDTO;
import farmacias.AppOchoa.dto.farmacia.FarmaciaResponseDTO;
import farmacias.AppOchoa.dto.farmacia.FarmaciaSimpleDTO;
import farmacias.AppOchoa.model.Farmacia;
import farmacias.AppOchoa.repository.FarmaciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FarmaciaService extends FarmaciaRepository {
    FarmaciaResponseDTO crear (Long farmaciaId,FarmaciaCreateDTO dto);
    FarmaciaResponseDTO buscarId(Long farmaciaId, Long id);
    Page<FarmaciaSimpleDTO> listarFarmacias(Long farmaciaId, Pageable pageable);
    void eliminar(Long farmaciaId, Long id);

}
