package farmacias.AppOchoa.dto.producto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoCreateDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre del producto no debe tener mas de 150 caracteres")
    private String nombre;

    @Size(max = 50, message = "El codigo de barras no debe tener mas de 50 caracteres")
    private String codigoBarras;

    private Long categoriaId;

    private Long presentacionId;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato inválido (máx 8 enteros, 2 decimales)")
    private BigDecimal precioCompra;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato inválido (máx 8 enteros, 2 decimales)")
    private BigDecimal precioVenta;

    private Boolean requiereReceta;
}
