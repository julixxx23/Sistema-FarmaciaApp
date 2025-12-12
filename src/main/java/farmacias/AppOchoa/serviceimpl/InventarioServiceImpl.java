package farmacias.AppOchoa.serviceimpl;


import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.dto.inventario.InventarioSimpleDTO;
import farmacias.AppOchoa.dto.inventariolotes.InventarioLotesUpdateDTO;
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
            SucursalRepository sucursalRepository){
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public InventarioResponseDTO crear (InventarioCreateDTO dto){
        if(inventarioRepository.existsByProductoIdAndSucursalId(dto.getProductoId(), dto.getSucursalId())){
            throw new RuntimeException("Ya existe el inventario del producto en sucursal: " +dto.getProductoId()  + "en sucursal" + dto.getSucursalId());
        }

        Producto producto = buscarProducto(dto.getProductoId());
        Sucursal sucursal = buscarSucursal(dto.getSucursalId());

        Inventario inventario = Inventario.builder()
                .inventarioCantidadActual(dto.getCantidadActual())
                .inventarioCantidadMinima(dto.getCantidadMinima())
                .producto(producto)
                .sucursal(sucursal)
                .build();

        Inventario guardar = inventarioRepository.save(inventario);

        return InventarioResponseDTO.fromEntity(guardar);


    }
    // Métodos auxiliares para buscar relaciones
    private Producto buscarProducto(Long productoId){
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado por ID: " +productoId));
    }

    private Sucursal buscarSucursal(Long sucursalId){
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " +sucursalId));
    }

    @Override
    public InventarioResponseDTO listaPorId (Long id){
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de inventarios no encontrada por ID: " +id));

        return InventarioResponseDTO.fromEntity(inventario);
    }

    @Override
    public List<InventarioSimpleDTO> listarTodos(){
        List<Inventario> inventarios = inventarioRepository.findAll();

        return inventarios.stream()
                .map(InventarioSimpleDTO:: fromEntity)
                .collect(Collectors.toList());

    }
    @Override
    public List<InventarioSimpleDTO> listarActivos(){
        List<Inventario> inventarios = inventarioRepository.findByInventarioEstadoTrue();

        return inventarios.stream()
                .map(InventarioSimpleDTO :: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public InventarioResponseDTO actualizar(Long id, InventarioLotesUpdateDTO dto){
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado por ID: " +id));

        inventario.setInventarioCantidadActual(dto.getCantidadActual());
        inventario.setInventarioCantidadMinima(dto.getCantidadMinima());

        Inventario guardar = inventarioRepository.save(inventario);

        return InventarioResponseDTO.fromEntity(guardar);

    }

    @Override
    public void cambiaEstado(Long id, Boolean estado){
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado por ID: " +id));
        // Actualiza estado
        if (!estado) {
            inventario.setInventarioCantidadActual(0); // "Inactivar" poniendo stock a 0
        }
        // Guarda estado
        inventarioRepository.save(inventario);
    }

    @Override
    public void eliminar(Long id){
        cambiaEstado(id, false);
    }













}