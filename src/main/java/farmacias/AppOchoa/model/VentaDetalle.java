package farmacias.AppOchoa.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "venta_detalle")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_id", nullable = false)
    private Long detalleId;

    @Column(name = "detalle_cantidad", nullable = false)
    private Integer detalleCantidad;

    @Column(name = "detalle_precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal detallePrecioUnitario;

    @Column(name = "detalle_subtotal", nullable = false)
    private BigDecimal detalleSubtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private InventarioLotes lote;
}
