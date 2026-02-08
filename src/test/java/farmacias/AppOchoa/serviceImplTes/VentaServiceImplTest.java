package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.model.Venta;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.repository.VentaRepository;
import farmacias.AppOchoa.serviceimpl.VentaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VentaServiceImplTest {
    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @InjectMocks
    private VentaServiceImpl ventaService;
    @Test
    @DisplayName("Debebria crear una correcta venta, con los datos ingresados")
    void ventaExitosa(){
        VentaCreateDTO dto = new VentaCreateDTO();
        dto.setSucursalId(1L);
        dto.setUsuarioId(1L);
        dto.setDetalles(new ArrayList<>());

        Sucursal sucursal = new Sucursal();
        sucursal.setSucursalId(1L);
        sucursal.setSucursalNombre("Farmacia Central Ochoa");

        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setUsuarioNombre("Pamela");
        usuario.setUsuarioApellido("Ochoa");

        Venta venta = Venta.builder()
                .ventaId(1L)
                .ventaFecha(LocalDate.of(2026, 2, 8).atStartOfDay())
                .ventaNumeroFactura("8949404")
                .ventaNumeroAutorizacion("00002")
                .ventaNitCliente("12849493")
                .ventaNombreCliente("Julian Orellana")
                .ventaSubtotal(BigDecimal.valueOf(250))
                .ventaDescuento(BigDecimal.valueOf(25))
                .ventaTotal(BigDecimal.valueOf(225))
                .sucursal(sucursal)
                .usuario(usuario)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        //ACT
        VentaResponseDTO resultado = ventaService.crear(dto);
        //ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getVentaId());
        verify(ventaRepository).save(any(Venta.class));
   }
    @Test
    @DisplayName("Deberia de buscar una venta por ID correctamente ")
    void busquedaVentaExitosa(){
        Long id = 1L;
        Venta venta = new Venta();
        venta.setVentaId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        VentaResponseDTO resultado = ventaService.listarPorId(id);
        //ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getVentaId());
        verify(ventaRepository).findById(1L);
    }


}
