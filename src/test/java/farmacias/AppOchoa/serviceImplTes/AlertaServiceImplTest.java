package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.alerta.AlertaCreateDTO;
import farmacias.AppOchoa.dto.alerta.AlertaResponseDTO;
import farmacias.AppOchoa.dto.alerta.AlertaUpdateDTO;
import farmacias.AppOchoa.exception.ResourceNotFoundException;
import farmacias.AppOchoa.model.Alerta;
import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.AlertaRepository;
import farmacias.AppOchoa.repository.InventarioLotesRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.serviceimpl.AlertaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertaServiceImplTest {
    @Mock
    private AlertaRepository alertaRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private InventarioLotesRepository inventarioLotesRepository;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private AlertaServiceImpl alertaService;

    //TEST CREACIÓN ALERTA
    @Test
    @DisplayName("Deberia crear una alerta correcta con los datos ingresados")
    void crearAlertaExitosa(){
        AlertaCreateDTO dto = new AlertaCreateDTO();
        dto.setLoteId(1L);
        dto.setMensaje("El stock de Suerox sabor fresa, se esta agotando");
        dto.setSucursalId(1L);
        dto.setProductoId(1L);

        Sucursal sucursal = new Sucursal();
        sucursal.setSucursalId(1L);
        sucursal.setSucursalNombre("Farmacia Central Ochoa");

        InventarioLotes inventarioLotes = new InventarioLotes();
        inventarioLotes.setLoteId(1L);
        inventarioLotes.setLoteCantidadActual(12);

        Producto producto = new Producto();
        producto.setProductoId(1L);
        producto.setProductoNombre("Suerox fresa 500 ml.");

        Alerta alertaGuardada = new Alerta();
        alertaGuardada.setAlertaId(1L);
        alertaGuardada.setAlertaMensaje(dto.getMensaje());

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioLotesRepository.findById(1L)).thenReturn(Optional.of(inventarioLotes));
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alertaGuardada);
        //ACT
        AlertaResponseDTO resultado = alertaService.crear(dto);
        //ASSERT
        assertNotNull(resultado);
        assertEquals("El stock de Suerox sabor fresa, se esta agotando", resultado.getMensaje());
        verify(alertaRepository).save(any(Alerta.class));
    }
    @Test
    @DisplayName("Deberia de eliminar una alerta correctamente")
    void eliminarAlertaExitosa(){
        Long id = 1L;
        Alerta alerta = new Alerta();
        alerta.setAlertaId(id);
        when(alertaRepository.existsById(id)).thenReturn(true);
        //ACT
        alertaService.eliminar(id);
        //ASSERT
        verify(alertaRepository).deleteById(id);
    }
    @Test
    @DisplayName("Deberia de lanzar una excepcion al eliminar una alerta por ID no encontrado")
    void alertaEliminarFalloId(){
        Long id =1L;
        when(alertaRepository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            alertaService.eliminar(id);
        });


    }

}
