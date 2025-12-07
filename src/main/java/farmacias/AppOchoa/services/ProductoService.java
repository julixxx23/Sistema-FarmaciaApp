package farmacias.AppOchoa.services;

import farmacias.AppOchoa.dto.producto.ProductoCreateDTO;
import farmacias.AppOchoa.dto.producto.ProductoResponseDTO;
import farmacias.AppOchoa.dto.producto.ProductoSimpleDTO;
import farmacias.AppOchoa.dto.producto.ProductoUpdateDTO;

import java.util.List;


public interface ProductoService  {
    ProductoResponseDTO agregarProducto(ProductoCreateDTO dto);
    ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO dto);
    void eliminarProducto(Long id);
    void cambiarEstado(Long id, Boolean nuevoEstado);
    ProductoResponseDTO obtenerPorCodigoBarras(String productoCodigoBarras);
    ProductoResponseDTO obtenerPorId(Long id);
    List<ProductoSimpleDTO> listarProductosActivos();



}
