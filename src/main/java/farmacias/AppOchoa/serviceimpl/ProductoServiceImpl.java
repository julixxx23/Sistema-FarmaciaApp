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
        // 1. Validar unicidad de nombre
        if (productoRepository.existsByProductoNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un producto con ese nombre");
        }

        // 2. Validar unicidad de código de barras (si se proporciona)
        if (dto.getCodigoBarras() != null && !dto.getCodigoBarras().isBlank() &&
                productoRepository.existsByProductoCodigoBarras(dto.getCodigoBarras())) {
            throw new RuntimeException("Ya existe un producto con ese código de barras");
        }

        // 3. Buscar relaciones
        Categoria categoria = buscarCategoria(dto.getCategoriaId());
        Presentacion presentacion = buscarPresentacion(dto.getPresentacionId());

        // 4. Construir y guardar producto
        Producto producto = Producto.builder()
                .productoNombre(dto.getNombre())
                .productoCodigoBarras(dto.getCodigoBarras())
                .productoPrecioCompra(dto.getPrecioCompra())
                .productoPrecioVenta(dto.getPrecioVenta())
                .productoRequiereReceta(dto.getRequiereReceta() != null ?
                        dto.getRequiereReceta() : true) // Valor por defecto
                .productoEstado(true)
                .categoria(categoria)
                .presentacion(presentacion)
                .build();

        Producto guardado = productoRepository.save(producto);
        return ProductoResponseDTO.fromEntity(guardado);
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO dto) {
        // 1. Buscar producto existente
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Validar unicidad de nombre (excepto para este producto)
        if (!producto.getProductoNombre().equals(dto.getNombre()) &&
                productoRepository.existsByProductoNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe otro producto con ese nombre");
        }

        // 3. Validar unicidad de código de barras (excepto para este producto)
        if (dto.getCodigoBarras() != null &&
                !dto.getCodigoBarras().equals(producto.getProductoCodigoBarras()) &&
                productoRepository.existsByProductoCodigoBarras(dto.getCodigoBarras())) {
            throw new RuntimeException("Ya existe otro producto con ese código de barras");
        }

        // 4. Buscar y actualizar relaciones (si se proporcionan)
        if (dto.getCategoriaId() != null) {
            Categoria categoria = buscarCategoria(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }

        if (dto.getPresentacionId() != null) {
            Presentacion presentacion = buscarPresentacion(dto.getPresentacionId());
            producto.setPresentacion(presentacion);
        }

        // 5. Actualizar campos simples
        producto.setProductoNombre(dto.getNombre());
        producto.setProductoCodigoBarras(dto.getCodigoBarras());
        producto.setProductoPrecioCompra(dto.getPrecioCompra());
        producto.setProductoPrecioVenta(dto.getPrecioVenta());
        producto.setProductoEstado(dto.getEstado());

        Producto actualizado = productoRepository.save(producto);
        return ProductoResponseDTO.fromEntity(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        // Eliminación lógica cambiar estado
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setProductoEstado(false);
        productoRepository.save(producto);
    }

    @Override
    public void cambiarEstado(Long id, Boolean nuevoEstado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setProductoEstado(nuevoEstado);
        productoRepository.save(producto);
    }

    @Override
    public ProductoResponseDTO obtenerPorCodigoBarras(String productoCodigoBarras) {
        Producto producto = productoRepository.findByProductoCodigoBarras(productoCodigoBarras)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return ProductoResponseDTO.fromEntity(producto);
    }

    @Override
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return ProductoResponseDTO.fromEntity(producto);
    }

    @Override
    public List<ProductoSimpleDTO> listarProductosActivos() {
        List<Producto> productos = productoRepository.findByProductoEstadoTrue();
        return productos.stream()
                .map(ProductoSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares para buscar relaciones
    private Categoria buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private Presentacion buscarPresentacion(Long presentacionId) {
        if (presentacionId == null) {
            return null;
        }
        return presentacionRepository.findById(presentacionId)
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada"));
    }
}