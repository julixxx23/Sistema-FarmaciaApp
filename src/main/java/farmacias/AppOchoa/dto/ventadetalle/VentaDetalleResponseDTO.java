package farmacias.AppOchoa.dto.ventadetalle;

import farmacias.AppOchoa.dto.producto.ProductoSimpleDTO;
import farmacias.AppOchoa.model.VentaDetalle;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaDetalleResponseDTO {

    private Long detalleId;

    private ProductoSimpleDTO producto;

    private String loteNumero;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

    public static VentaDetalleResponseDTO fromEntity(VentaDetalle detalle) {
        return VentaDetalleResponseDTO.builder()
                .detalleId(detalle.getDetalleId())
                .producto(detalle.getProducto() != null ?
                        ProductoSimpleDTO.fromEntity(detalle.getProducto()) : null)
                .loteNumero(detalle.getLote() != null ?
                        detalle.getLote().getLoteNumero() : null)
                .cantidad(detalle.getDetalleCantidad())
                .precioUnitario(detalle.getDetallePrecioUnitario())
                .subtotal(detalle.getDetalleSubtotal())
                .build();
    }
}