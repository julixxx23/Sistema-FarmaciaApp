package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.model.Presentacion;
import farmacias.AppOchoa.repository.PresentacionRepository;
import farmacias.AppOchoa.serviceimpl.PresentacionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresentacionServiceImplTest {
    @Mock
    private PresentacionRepository presentacionRepository;
    @InjectMocks
    private PresentacionServiceImpl presentacionService;

    //TEST PARA AGREGAR PRESENTACION
    @Test
    @DisplayName("Deberia de crear una presentacion correctamente cuando los datos son validos")
    void crearPresentacionExitosa(){
        //ARRANGE
        PresentacionCreateDTO dto = new PresentacionCreateDTO();
        dto.setNombre("Caja de 12 Unidades");

        Presentacion presentacionGuardado = Presentacion.builder()
                .presentacionId(1L)
                .presentacionNombre("Caja de 12 Unidades")
                .presentacionEstado(true)
                .build();

        when(presentacionRepository.existsByPresentacionNombre(anyString())).thenReturn(false);
        when(presentacionRepository.save(ArgumentMatchers.any(Presentacion.class))).thenReturn(presentacionGuardado);

        //ACT
        PresentacionResponseDTO resultado = presentacionService.crear(dto);

        //ASSERT
        assertNotNull(resultado);
        assertEquals("Caja de 12 Unidades", resultado.getNombre());
        verify(presentacionRepository).save(ArgumentMatchers.any(Presentacion.class));
    }

}
