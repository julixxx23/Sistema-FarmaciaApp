package farmacias.AppOchoa.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "producto_nombre", nullable = false, length = 150)
    private String productoNombre;

    @Column(name = "producto_codigo_barras", unique = true,  length = 50)
    private String productoCodigoBarras;

    @Column(name = "producto_precio_compra", nullable = false)
    private BigDecimal productoPrecioCompra;

    @Column(name = "producto_precio_venta", nullable = false)
    private BigDecimal productoPrecioVenta;

    @Column(name = "producto_requiere_receta", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean productoRequiereReceta = true;

    @Column(name = "producto_estado", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean productoEstado = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentacion_id")
    private Presentacion presentacion;
}
