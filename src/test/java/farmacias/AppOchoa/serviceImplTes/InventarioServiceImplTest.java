package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.inventario.InventarioCreateDTO;
import farmacias.AppOchoa.dto.inventario.InventarioResponseDTO;
import farmacias.AppOchoa.model.Inventario;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.InventarioRepository;
import farmacias.AppOchoa.repository.ProductoRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.serviceimpl.InventarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceImplTest {
    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @InjectMocks
    private InventarioServiceImpl inventarioService;
    @Test
    @DisplayName("Deberia registrar un Item correctamente en el inventario")
    void crearRegistroExitoso(){
        InventarioCreateDTO dto = new InventarioCreateDTO();
        dto.setSucursalId(1L);
        dto.setProductoId(1L);
        dto.setCantidadActual(80);
        dto.setCantidadMinima(12);

        Sucursal sucursal = new Sucursal();
        sucursal.setSucursalId(1L);
        sucursal.setSucursalNombre("Farmacia Central Ochoa");

        Producto producto = new Producto();
        producto.setProductoId(1L);
        producto.setProductoNombre("Acetaminofen Bayer");


        Inventario inventario = Inventario.builder()
                .inventarioId(1L)
                .inventarioCantidadActual(80)
                .inventarioCantidadMinima(12)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
        //ACT
        InventarioResponseDTO resultado = inventarioService.crear(dto);
        //ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getInventarioId());
        verify(inventarioRepository).save(any(Inventario.class));



    }

}
