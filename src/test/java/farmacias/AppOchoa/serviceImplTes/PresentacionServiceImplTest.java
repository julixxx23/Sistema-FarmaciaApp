package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.presentacion.PresentacionCreateDTO;
import farmacias.AppOchoa.dto.presentacion.PresentacionResponseDTO;
import farmacias.AppOchoa.model.Presentacion;
import farmacias.AppOchoa.repository.PresentacionRepository;
import farmacias.AppOchoa.serviceimpl.PresentacionServiceImpl;
import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmSetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Deberia de lanzar una excepcion si ya existe una presentacion con ese nombre")
    void crearPresentacionFallaNombreDuplicado(){
        //ARRANGE
        PresentacionCreateDTO dto = new PresentacionCreateDTO();
        dto.setNombre("Tabletas de 10 Unidades");

        Presentacion presentacionGuarda = Presentacion.builder()
                .presentacionId(1L)
                .presentacionNombre("Tabletas de 10 Unidades")
                .presentacionEstado(true)
                .build();

        when(presentacionRepository.existsByPresentacionNombre(any())).thenReturn(true);

        //ASSERT & ACT
        assertThrows(RuntimeException.class, () ->{
            presentacionService.crear(dto);
        });
        verify(presentacionRepository, never()).save(any(Presentacion.class));

    }

    @Test
    @DisplayName("Deberia lanzar una excepcion si buscamos un ID que no existe")
    void obtenerPorIdNoEncontrado(){
        Long idNoExistente = 1l;
        when(presentacionRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThrows(RuntimeException.class, () ->
            presentacionService.obtenerPorId(idNoExistente));
        }


    @Test
    @DisplayName("Eliminar una presentacion deberia cambiar a estado false (Borrado Logico")
    void eliminarPresentacion_CambiarEstado(){
        //ARRANGE
        Long id = 1L;
        Presentacion presentacionMock = new Presentacion();
        presentacionMock.setPresentacionId(id);
        presentacionMock.setPresentacionEstado(true);

        when(presentacionRepository.findById(id)).thenReturn(Optional.of(presentacionMock));
        //ACT
        presentacionService.eliminar(id);
        //ASSERT
        ArgumentCaptor<Presentacion> captor = ArgumentCaptor.forClass(Presentacion.class);
        verify(presentacionRepository).save(captor.capture());
        assertFalse(captor.getValue().getPresentacionEstado(), "El estado deberia de haber cambiado a false");
    }

}



