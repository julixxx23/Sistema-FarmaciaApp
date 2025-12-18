package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesCreateDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesResponseDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.LoteEstado;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.InventarioLotesRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.InventarioLotesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioLotesServiceImpl implements InventarioLotesService {

    private final InventarioLotesRepository inventarioLotesRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public InventarioLotesServiceImpl(
            InventarioLotesRepository inventarioLotesRepository,
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository) {
        this.inventarioLotesRepository = inventarioLotesRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    // 1. CREAR
    @Override
    public InventarioLotesResponseDTO crear(InventarioLotesCreateDTO dto) {
        if (inventarioLotesRepository.existsByLoteNumeroAndSucursalId(dto.getNumeroLote(), dto.getSucursalId())) {
            throw new RuntimeException("El número de lote " + dto.getNumeroLote() + " ya existe en esta sucursal.");
        }

        Producto producto = buscarProducto(dto.getProductoId());
        Sucursal sucursal = buscarSucursal(dto.getSucursalId());

        InventarioLotes inventarioLotes = InventarioLotes.builder()
                .loteNumero(dto.getNumeroLote())
                .loteCantidadInicial(dto.getCantidadInicial())
                .loteCantidadActual(dto.getCantidadInicial()) // Sincronización inicial
                .loteFechaVencimiento(dto.getFechaVencimiento())
                .lotePrecioCompra(dto.getPrecioCompra())
                .loteEstado(LoteEstado.disponible)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        InventarioLotes guardar = inventarioLotesRepository.save(inventarioLotes);
        return InventarioLotesResponseDTO.fromEntity(guardar);
    }

    // 2. LISTAR (ID, Todos, Activos)
    @Override
    public InventarioLotesResponseDTO listarPorId(Long id) {
        InventarioLotes inventarioLotes = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario Lotes no encontrado por ID: " + id));
        return InventarioLotesResponseDTO.fromEntity(inventarioLotes);
    }

    @Override
    public List<InventarioLotesSimpleDTO> listarTodos() {
        return inventarioLotesRepository.findAll().stream()
                .map(InventarioLotesSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioLotesSimpleDTO> listarActivos() {
        return inventarioLotesRepository.findByLoteEstado(LoteEstado.disponible).stream()
                .map(InventarioLotesSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 3. ACTUALIZAR
    @Override
    public InventarioLotesResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto) {
        InventarioLotes inventarioLotes = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario Lotes no encontrado por ID: " + id));

        // Se actualiza la cantidad actual (la disponible para la venta)
        inventarioLotes.setLoteCantidadActual(dto.getCantidadActual());

        if (dto.getCantidadActual() <= 0) {
            inventarioLotes.setLoteEstado(LoteEstado.agotado);
        }

        InventarioLotes guardar = inventarioLotesRepository.save(inventarioLotes);
        return InventarioLotesResponseDTO.fromEntity(guardar);
    }

    // 4. ESTADO
    @Override
    public void cambiarEstado(Long id, Boolean estado) {
        InventarioLotes inventarioLotes = inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario Lotes no encontrado por ID: " + id));

        if (Boolean.TRUE.equals(estado)) {
            inventarioLotes.setLoteEstado(LoteEstado.disponible);
        } else {
            inventarioLotes.setLoteEstado(LoteEstado.vencido);
        }

        inventarioLotesRepository.save(inventarioLotes);
    }

    // 5. ELIMINAR
    @Override
    public void eliminar(Long id) {
        cambiarEstado(id, false);
    }

    // 6. AUXILIARES
    private Producto buscarProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado por ID: " + productoId));
    }

    private Sucursal buscarSucursal(Long sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + sucursalId));
    }
}