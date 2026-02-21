package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.Caja;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.CajaRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.CajaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaServiceImpl implements CajaService {
    private final CajaRepository cajaRepository;
    private final SucursalRepository sucursalRepository;

    public CajaServiceImpl(
            CajaRepository cajaRepository,
            SucursalRepository sucursalRepository){
        this.cajaRepository = cajaRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public CajaResponseDTO crearCaja(CajaCreateDTO dto){
        if(cajaRepository.existsBySucursalIdAndCajaNombre(dto.getSucursalId(), dto.getCajaNombre())){
            throw new RuntimeException("Ya existe una caja con este nombre");
        }
        Sucursal sucursal =  buscarSucursal(dto.getSucursalId());

        Caja caja = Caja.builder()
                .cajaNombre(dto.getCajaNombre())
                .sucursal(sucursal)
                .build();

        return CajaResponseDTO.fromEntity(cajaRepository.save(caja));
    }
    private Sucursal buscarSucursal(Long id){
        if(id == null) return  null;
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID"));
    }

    @Override
    @Transactional(readOnly = true)
    public CajaResponseDTO buscarPorId(Long id){
        return cajaRepository.findById(id)
                .map(CajaResponseDTO:: fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada por ID"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CajaSimpleDTO> listarCajasActivas(Pageable pageable){
        return cajaRepository.findAll(pageable)
                .map(CajaSimpleDTO::fromEntity);
    }


}
