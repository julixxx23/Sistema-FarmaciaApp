package farmacias.AppOchoa.dto.farmacia;

import farmacias.AppOchoa.model.PlanTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FarmaciaCreateDTO {
    @NotBlank(message = "El nombre de la farmacia es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debera tener entre 2 y 50 caracteres")
    private String farmaciaNombre;

    @NotBlank(message = "El nit de la farmacia es obligatorio")
    @Size(max = 9, message = "El  nit debera tener 9 caracteres")
    private String farmaciaNit;

    @NotBlank(message = "El email de la farmacia es obligatorio")
    private String farmaciaEmail;

    @NotBlank(message = "El telefono de la farmacia es obligatorio")
    private String farmaciaTelefono;

    @NotNull(message = "La asignacion del plan es obligatoria")
    private PlanTipo planTipo;
    private LocalDate pruebaHasta;
    private LocalDate suscripcionVigencia;


}
