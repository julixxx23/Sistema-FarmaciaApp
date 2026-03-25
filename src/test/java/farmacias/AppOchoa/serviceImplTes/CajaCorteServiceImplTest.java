package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.cajacorte.CajaCorteCreateDTO;
import farmacias.AppOchoa.dto.cajacorte.CajaCorteResponseDTO;
import farmacias.AppOchoa.model.CajaCorte;
import farmacias.AppOchoa.model.CajaSesiones;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.CajaCortesRepository;
import farmacias.AppOchoa.repository.CajaSesionesRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.repository.VentaPagoRepository;
import farmacias.AppOchoa.serviceimpl.CajaCorteServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CajaCorteServiceImplTest {

    @Mock
    private CajaCortesRepository cajaCortesRepository;
    @Mock
    private CajaSesionesRepository cajaSesionesRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private VentaPagoRepository ventaPagoRepository;
    @InjectMocks
    private CajaCorteServiceImpl cajaCorteService;

    @Test
    @DisplayName("Deberia crear un corte de caja correctamente")
    void crearCajaCortes(){

        Long farmaciaId = 1L;

        //ASSERT
        CajaCorteCreateDTO dto = new CajaCorteCreateDTO();
        dto.setSesionId(1L);
        dto.setUsuarioSupervisorId(1L);
        dto.setEfectivoFisicoContado(new BigDecimal(800.00));

        CajaSesiones cajaSesiones = new CajaSesiones();
        cajaSesiones.setSesionId(1L);

        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);

        CajaCorte cajaCorte = new CajaCorte();
        cajaCorte.setCorteId(1L);
        cajaCorte.setCorteTotalEfectivo(new BigDecimal(800.00));

        when(cajaSesionesRepository.findById(1L)).thenReturn(Optional.of(cajaSesiones));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ventaPagoRepository.sumarPorSesionYMetodo(any(), any())).thenReturn(BigDecimal.ZERO);
        when(ventaPagoRepository.sumarTotalPorSesion(any())).thenReturn(BigDecimal.ZERO);
        when(cajaCortesRepository.save(any(CajaCorte.class))).thenReturn(cajaCorte);

        CajaCorteResponseDTO resultado = cajaCorteService.crear(farmaciaId, dto);

        assertNotNull(resultado);
        assertEquals(dto.getEfectivoFisicoContado(), resultado.getCorteTotalEfectivo());

        ArgumentCaptor<CajaCorte> cajaCorteArgumentCaptor = ArgumentCaptor.forClass(CajaCorte.class);
        verify(cajaCortesRepository).save(cajaCorteArgumentCaptor.capture());
    }


}
