package farmacias.AppOchoa.dto.usuario;

import farmacias.AppOchoa.model.UsuarioRol;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @NotNull(message = "El rol es obligatorio")
    private UsuarioRol rol;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;
}