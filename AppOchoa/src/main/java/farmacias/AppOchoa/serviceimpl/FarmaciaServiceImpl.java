package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.farmacia.FarmaciaCreateDTO;
import farmacias.AppOchoa.dto.farmacia.FarmaciaResponseDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.Farmacia;
import farmacias.AppOchoa.model.PlanTipo;
import farmacias.AppOchoa.repository.FarmaciaRepository;
import farmacias.AppOchoa.services.FarmaciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FarmaciaServiceImpl implements FarmaciaService {
    private final FarmaciaRepository farmaciaRepository;

    public FarmaciaServiceImpl(FarmaciaRepository farmaciaRepository1){
        this.farmaciaRepository = farmaciaRepository1;
    }

    @Override
    public FarmaciaResponseDTO crear(FarmaciaCreateDTO dto){
        if(farmaciaRepository.existsByFarmaciaNit(dto.getFarmaciaNit())){
            throw new IllegalArgumentException("El nit ya esta en uso" + dto.getFarmaciaNit());
        }

        Farmacia farmacia = Farmacia.builder()
                .farmaciaNombre(dto.getFarmaciaNombre())
                .farmaciaNit(dto.getFarmaciaNit())
                .farmaciaEmail(dto.getFarmaciaEmail())
                .farmaciaTelefono(dto.getFarmaciaTelefono())
                .pruebaHasta(dto.getPruebaHasta())
                .planTipo(dto.getPlanTipo())
                .suscripcionVigencia(dto.getSuscripcionVigencia())
                .farmaciaActiva(true)
                .enPeriodoPrueba(true)
                .maxSucursales(resolverMaxSucursales(dto.getPlanTipo()))
                .maxUsuarios(resolverMaxUsuarios(dto.getPlanTipo()))
                .build();



        return FarmaciaResponseDTO.fromEntity(farmaciaRepository.save(farmacia));
    }
    private int resolverMaxSucursales(PlanTipo plan) {
        return switch (plan) {
            case basico -> 1;
            case pro -> 3;
            case chain -> 10;
        };
    }

    private int resolverMaxUsuarios(PlanTipo plan) {
        return switch (plan) {
            case basico -> 2;
            case pro -> 5;
            case chain -> 20;
        };
    }



}
