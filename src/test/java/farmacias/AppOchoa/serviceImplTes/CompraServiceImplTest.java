package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.model.Compra;
import farmacias.AppOchoa.model.CompraEstado;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.*;
import farmacias.AppOchoa.serviceimpl.CompraServiceImpl;
import org.hibernate.cache.spi.AbstractCacheTransactionSynchronization;
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
public class CompraServiceImplTest {
    @Mock
    private CompraRepository compraRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private CompraServiceImpl compraService;
    @Test
    @DisplayName("Deberia de crear una compra correctamente con los datos registrados")
    void crearCompraExitosa(){
        CompraCreateDTO dto = new CompraCreateDTO();
        dto.setFechaCompra(LocalDate.of(2024, 2, 6));
        dto.setSucursalId(1L);
        dto.setUsuarioId(1L);
        dto.setDetalles(new ArrayList<>());

        Sucursal sucursal = new Sucursal();
        sucursal.setSucursalId(1L);
        sucursal.setSucursalNombre("Farmacia Central Ochoa");

        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setNombreUsuarioUsuario("JuaSolu");
        usuario.setUsuarioNombre("Julian");

        Compra compra = Compra.builder()
                .compraId(1L)
                .compraFecha(LocalDate.of(2024, 2, 6))
                .compraObservaciones("Exitosa")
                .compraTotal(BigDecimal.valueOf(12.000))
                .compraEstado(CompraEstado.activa)
                .sucursal(sucursal)
                .usuario(usuario)
                .build();

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(compraRepository.save(any(Compra.class))).thenReturn(compra);
        //ACT
        CompraResponseDTO resultado = compraService.crear(dto);
        //ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getCompraId());
        verify(compraRepository).save(any(Compra.class));

    }
}
