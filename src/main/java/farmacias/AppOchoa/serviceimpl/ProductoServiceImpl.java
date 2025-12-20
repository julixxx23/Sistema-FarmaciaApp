package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.producto.ProductoCreateDTO;
import farmacias.AppOchoa.dto.producto.ProductoResponseDTO;
import farmacias.AppOchoa.dto.producto.ProductoSimpleDTO;
import farmacias.AppOchoa.dto.producto.ProductoUpdateDTO;
import farmacias.AppOchoa.model.Categoria;
import farmacias.AppOchoa.model.Presentacion;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.repository.CategoriaRepository;
import farmacias.AppOchoa.repository.PresentacionRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.services.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PresentacionRepository presentacionRepository;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            PresentacionRepository presentacionRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.presentacionRepository = presentacionRepository;
    }

    @Override
    public ProductoResponseDTO agregarProducto(ProductoCreateDTO dto) {
        // 1. Validaciones de unicidad
        if (productoRepository.existsByProductoNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + dto.getNombre());
        }

        if (dto.getCodigoBarras() != null && !dto.getCodigoBarras().isBlank() &&
                productoRepository.existsByProductoCodigoBarras(dto.getCodigoBarras())) {
            throw new RuntimeException("El código de barras ya está registrado: " + dto.getCodigoBarras());
        }

        // 2. Buscar entidades relacionadas
        Categoria categoria = buscarCategoria(dto.getCategoriaId());
        Presentacion presentacion = buscarPresentacion(dto.getPresentacionId());

        // 3. Construir producto con IVA
        Producto producto = Producto.builder()
                .productoNombre(dto.getNombre())
                .productoCodigoBarras(dto.getCodigoBarras())
                .productoPrecioCompra(dto.getPrecioCompra())
                .productoPrecioVenta(dto.getPrecioVenta())
                .productoIva(dto.getIva()) // <-- IVA Integrado
                .productoRequiereReceta(dto.getRequiereReceta() != null ? dto.getRequiereReceta() : false)
                .productoEstado(true)
                .categoria(categoria)
                .presentacion(presentacion)
                .build();

        return ProductoResponseDTO.fromEntity(productoRepository.save(producto));
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));

        // Validar unicidad de nombre si cambió
        if (!producto.getProductoNombre().equalsIgnoreCase(dto.getNombre()) &&
                productoRepository.existsByProductoNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe otro producto con el nombre: " + dto.getNombre());
        }

        // Validar unicidad de código de barras si cambió
        if (dto.getCodigoBarras() != null &&
                !dto.getCodigoBarras().equals(producto.getProductoCodigoBarras()) &&
                productoRepository.existsByProductoCodigoBarras(dto.getCodigoBarras())) {
            throw new RuntimeException("El código de barras ya pertenece a otro producto.");
        }

        // Actualizar relaciones y campos
        producto.setCategoria(buscarCategoria(dto.getCategoriaId()));
        producto.setPresentacion(buscarPresentacion(dto.getPresentacionId()));

        producto.setProductoNombre(dto.getNombre());
        producto.setProductoCodigoBarras(dto.getCodigoBarras());
        producto.setProductoPrecioCompra(dto.getPrecioCompra());
        producto.setProductoPrecioVenta(dto.getPrecioVenta());
        producto.setProductoIva(dto.getIva()); // <-- IVA actualizado

        if (dto.getEstado() != null) producto.setProductoEstado(dto.getEstado());
        if (dto.getRequiereReceta() != null) producto.setProductoRequiereReceta(dto.getRequiereReceta());

        return ProductoResponseDTO.fromEntity(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Long id) {
        cambiarEstado(id, false);
    }

    @Override
    public void cambiarEstado(Long id, Boolean nuevoEstado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));
        producto.setProductoEstado(nuevoEstado);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorCodigoBarras(String codigo) {
        return productoRepository.findByProductoCodigoBarras(codigo)
                .map(ProductoResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(ProductoResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoSimpleDTO> listarProductosActivos(Pageable pageable) {
        return productoRepository.findByProductoEstadoTrue(pageable)
                .map(ProductoSimpleDTO::fromEntity);
    }

    // Métodos auxiliares privados
    private Categoria buscarCategoria(Long id) {
        if (id == null) return null;
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada ID: " + id));
    }

    private Presentacion buscarPresentacion(Long id) {
        if (id == null) return null;
        return presentacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada ID: " + id));
    }
}