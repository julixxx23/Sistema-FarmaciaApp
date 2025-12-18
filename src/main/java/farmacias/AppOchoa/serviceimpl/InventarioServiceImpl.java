package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventario.InventarioUpdateDTO;
import farmacias.AppOchoa.model.*;
import farmacias.AppOchoa.repository.InventarioRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.services.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public InventarioServiceImpl(
            InventarioRepository inventarioRepository,
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public InventarioResponseDTO crear(InventarioCreateDTO dto) {
        // CORREGIDO: Cambio del método antiguo al nuevo
        if (inventarioRepository.existsByProducto_ProductoIdAndSucursal_SucursalId(dto.getProductoId(), dto.getSucursalId())) {
            throw new RuntimeException("Ya existe un registro de inventario para este producto en la sucursal seleccionada.");
        }

        Producto producto = buscarProducto(dto.getProductoId());
        Sucursal sucursal = buscarSucursal(dto.getSucursalId());

        Inventario inventario = Inventario.builder()
                .inventarioCantidadActual(dto.getCantidadActual())
                .inventarioCantidadMinima(dto.getCantidadMinima())
                .producto(producto)
                .sucursal(sucursal)
                .build();

        return InventarioResponseDTO.fromEntity(inventarioRepository.save(inventario));
    }

    @Override
    public InventarioResponseDTO listaPorId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
        return InventarioResponseDTO.fromEntity(inventario);
    }

    @Override
    public List<InventarioSimpleDTO> listarTodos() {
        return inventarioRepository.findAll().stream()
                .map(InventarioSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioSimpleDTO> listarActivos() {
        return inventarioRepository.findAll().stream()
                .filter(i -> i.getInventarioCantidadActual() > 0)
                .map(InventarioSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public InventarioResponseDTO actualizar(Long id, InventarioUpdateDTO dto) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));

        inventario.setInventarioCantidadActual(dto.getCantidadActual());
        inventario.setInventarioCantidadMinima(dto.getCantidadMinima());

        return InventarioResponseDTO.fromEntity(inventarioRepository.save(inventario));
    }

    @Override
    public void cambiaEstado(Long id, Boolean estado) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));

        if (Boolean.FALSE.equals(estado)) {
            inventario.setInventarioCantidadActual(0);
        }
        inventarioRepository.save(inventario);
    }

    @Override
    public void eliminar(Long id) {
        cambiaEstado(id, false);
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    private Sucursal buscarSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }
}