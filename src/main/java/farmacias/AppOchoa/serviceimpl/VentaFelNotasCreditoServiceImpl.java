package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoCreateDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoResponseDTO;
import farmacias.AppOchoa.dto.ventafelnotascredito.VentaFelNotasCreditoSimpleDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.VentaFel;
import farmacias.AppOchoa.model.VentaFelNotasCredito;
import farmacias.AppOchoa.repository.VentaFelNotasCreditoRepository;
import farmacias.AppOchoa.repository.VentaFelRepository;
import farmacias.AppOchoa.services.VentaFelNotasCreditoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VentaFelNotasCreditoServiceImpl implements VentaFelNotasCreditoService {
    private final VentaFelNotasCreditoRepository ventaFelNotasCreditoRepository;
    private final VentaFelRepository ventaFelRepository;

    public VentaFelNotasCreditoServiceImpl(
            VentaFelNotasCreditoRepository ventaFelNotasCreditoRepository,
            VentaFelRepository ventaFelRepository){
        this.ventaFelNotasCreditoRepository = ventaFelNotasCreditoRepository;
        this.ventaFelRepository = ventaFelRepository;
    }
    @Override
    public VentaFelNotasCreditoResponseDTO crear(VentaFelNotasCreditoCreateDTO dto){
        VentaFel ventaFel = buscarVentas(dto.getFelId());

        VentaFelNotasCredito ventaFelNotasCredito = VentaFelNotasCredito.builder()
                .ventaFel(ventaFel)
                .notaMotivo(dto.getNotaMotivo())
                .build();

        return VentaFelNotasCreditoResponseDTO.fromEntity(ventaFelNotasCreditoRepository.save(ventaFelNotasCredito));
    }
    private VentaFel buscarVentas(Long id){
        if(id == null) return null;
        return ventaFelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta fel no encontrada ID"));
    }
    @Override
    @Transactional(readOnly = true)
    public VentaFelNotasCreditoResponseDTO buscarPorId(Long id){
        return ventaFelNotasCreditoRepository.findById(id)
                .map(VentaFelNotasCreditoResponseDTO:: fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Nota credito no encontrada por ID"));
    }
    @Override
    @Transactional(readOnly = true)
    public Page<VentaFelNotasCreditoSimpleDTO> listarNotas(Pageable pageable){
        return ventaFelNotasCreditoRepository.findAll(pageable)
                .map(VentaFelNotasCreditoSimpleDTO:: fromEntity);
    }
    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Por reglas de auditoría financiera, este registro es histórico y no puede ser eliminado ni modificado.");
    }

}
