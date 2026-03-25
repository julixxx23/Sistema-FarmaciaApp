package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.suscripcionpago.SuscripcionPagoCreateDTO;
import farmacias.AppOchoa.model.PagoMetodo;
import farmacias.AppOchoa.model.SuscripcionPago;
import farmacias.AppOchoa.repository.FarmaciaRepository;
import farmacias.AppOchoa.repository.SuscripcionPagoRepository;
import farmacias.AppOchoa.services.SuscripcionPagoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class SuscripcionPagoServiceImplTest {
    @Mock
    private SuscripcionPagoRepository suscripcionPagoRepository;
    @Mock
    private FarmaciaRepository farmaciaRepository;
    @InjectMocks
    private SuscripcionPagoService suscripcionPagoService;

    @Test
    @DisplayName(("Deberia de crear una suscripcion correctamente"))
    void crearSuscripcion(){
        Long farmaciaId = 1L;
        //DTO
        SuscripcionPagoCreateDTO dto = new SuscripcionPagoCreateDTO();
        dto.setPagoMonto(new BigDecimal(600.00));
        dto.setPagoMetodo(PagoMetodo.efectivo);


    }
}
