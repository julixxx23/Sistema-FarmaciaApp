package farmacias.AppOchoa.dto.producto;

import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionSimpleDTO;
import farmacias.AppOchoa.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoResponseDTO {

    private Long productoId;
    private String nombre;
    private String codigoBarras;
    private CategoriaSimpleDTO categoria;
    private PresentacionSimpleDTO presentacion;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Boolean requiereReceta;
    private Boolean estado;

    public static ProductoResponseDTO fromEntity (farmacias.AppOchoa.model.Producto producto) {
        return ProductoResponseDTO.builder()
                .productoId(producto.getProductoId())
                .nombre(producto.getProductoNombre())
                .codigoBarras(producto.getProductoCodigoBarras())
                .categoria(producto.getCategoria() != null ?
                        CategoriaSimpleDTO.fromEntity(producto.getCategoria()) : null)
                .presentacion(producto.getPresentacion() != null ?
                        PresentacionSimpleDTO.fromEntity(producto.getPresentacion()) : null)
                .precioCompra(producto.getProductoPrecioCompra())
                .precioVenta(producto.getProductoPrecioVenta())
                .requiereReceta(producto.getProductoRequiereReceta())
                .estado(producto.getProductoEstado())
                .build();


    }
}
