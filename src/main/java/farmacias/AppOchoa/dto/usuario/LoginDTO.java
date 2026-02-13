package farmacias.AppOchoa.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Schema(description = "Nombre de usuario del sistema", example = "admin")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @Schema(description = "Contraseña secreta", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}