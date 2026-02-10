package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.LoteEstado;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.InventarioLotesRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.InventarioLotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class InventarioLotesServiceImpl implements InventarioLotesService {

    private final InventarioLotesRepository inventarioLotesRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public InventarioLotesResponseDTO crear(InventarioLotesCreateDTO dto) {
        if (inventarioLotesRepository.existsByLoteNumeroAndSucursal_SucursalId(dto.getNumeroLote(), dto.getSucursalId())) {
            throw new ResourceNotFoundException("El número de lote " + dto.getNumeroLote() + " ya existe en esta sucursal.");
        }

        Producto producto = buscarProducto(dto.getProductoId());
        Sucursal sucursal = buscarSucursal(dto.getSucursalId());

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
    public InventarioLotesResponseDTO buscarPorId(Long id) {
        InventarioLotes lote = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote no encontrado ID: " + id));
        return InventarioLotesResponseDTO.fromEntity(lote);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioLotesSimpleDTO> listarPorSucursalPaginado(Long sucursalId, Pageable pageable) {
        return inventarioLotesRepository.findBySucursal_SucursalId(sucursalId, pageable)
                .map(InventarioLotesSimpleDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioLotesSimpleDTO> listarProximosAVencerPaginado(LocalDate fechaLimite, Pageable pageable) {
        return inventarioLotesRepository.findByLoteFechaVencimientoLessThanEqual(fechaLimite, pageable)
                .map(InventarioLotesSimpleDTO::fromEntity);
    }

    @Override
    public InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto) {
        InventarioLotes lote = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote no encontrado ID: " + id));

        lote.setLoteCantidadActual(dto.getCantidadActual());
        lote.setLoteEstado(dto.getEstado());

        return InventarioLotesResponseDTO.fromEntity(inventarioLotesRepository.save(lote));
    }

    @Override
    public void eliminar(Long id) {
        if (!inventarioLotesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lote no encontrado ID: " + id);
        }
        inventarioLotesRepository.deleteById(id);
    }

    // Métodos auxiliares privados
    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado ID: " + id));
    }

    private Sucursal buscarSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada ID: " + id));
    }
}