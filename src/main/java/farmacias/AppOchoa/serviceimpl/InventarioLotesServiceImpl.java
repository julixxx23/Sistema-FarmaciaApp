package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.LoteEstado;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.InventarioLotesRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.InventarioLotesService;
import farmacias.AppOchoa.services.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioLotesServiceImpl implements InventarioLotesService {

    private final InventarioLotesRepository inventarioLotesRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    @Transactional
    public InventarioLotesResponseDTO crear(InventarioLotesCreateDTO dto) {
        // CORRECCIÓN LÍNEA 41: Nombre coincide con el Repository corregido
        if (inventarioLotesRepository.existsByLoteNumeroAndSucursal_SucursalId(dto.getNumeroLote(), dto.getSucursalId())) {
            throw new RuntimeException("El número de lote " + dto.getNumeroLote() + " ya existe en esta sucursal.");
        }

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        InventarioLotes lote = InventarioLotes.builder()
                .loteNumero(dto.getNumeroLote())
                .loteCantidadInicial(dto.getCantidadInicial())
                .loteCantidadActual(dto.getCantidadInicial())
                .loteFechaVencimiento(dto.getFechaVencimiento())
                .lotePrecioCompra(dto.getPrecioCompra())
                .loteEstado(LoteEstado.disponible)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        return InventarioLotesResponseDTO.fromEntity(inventarioLotesRepository.save(lote));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioLotesResponseDTO> listarPorSucursal(Long sucursalId) {
        return inventarioLotesRepository.findBySucursal_SucursalId(sucursalId)
                .stream()
                .map(InventarioLotesResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioLotesResponseDTO buscarPorId(Long id) {
        InventarioLotes lote = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
        return InventarioLotesResponseDTO.fromEntity(lote);
    }

    @Override
    @Transactional
    public InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto) {
        InventarioLotes lote = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
        lote.setLoteCantidadActual(dto.getCantidadActual());
        lote.setLoteEstado(dto.getEstado());
        return InventarioLotesResponseDTO.fromEntity(inventarioLotesRepository.save(lote));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioLotesResponseDTO> listarProximosAVencer(LocalDate fechaLimite) {
        return inventarioLotesRepository.findByLoteFechaVencimientoLessThanEqual(fechaLimite)
                .stream()
                .map(InventarioLotesResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        inventarioLotesRepository.deleteById(id);
    }
}