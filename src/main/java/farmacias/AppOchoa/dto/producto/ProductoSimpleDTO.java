package farmacias.AppOchoa.dto.producto;

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

public class ProductoSimpleDTO {
    private Long productoId;
    private String nombre;
    private BigDecimal precioVenta;

    public static ProductoSimpleDTO fromEntity(Producto producto){
        return ProductoSimpleDTO.builder()
                .productoId(producto.getProductoId())
                .nombre((producto.getProductoNombre()))
                .precioVenta(producto.getProductoPrecioVenta())
                .build();
    }
}
