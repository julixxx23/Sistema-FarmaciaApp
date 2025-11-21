package farmacias.AppOchoa.dto.venta;

import farmacias.AppOchoa.model.VentaEstado;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaUpdateDTO {

    @NotNull(message = "El estado es obligatorio")
    private VentaEstado estado;
}