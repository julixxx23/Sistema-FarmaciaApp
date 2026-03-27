package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.caja.CajaCreateDTO;
import farmacias.AppOchoa.dto.caja.CajaResponseDTO;
import farmacias.AppOchoa.dto.caja.CajaSimpleDTO;
import farmacias.AppOchoa.model.Caja;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.CajaRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.serviceimpl.CajaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CajaServiceImplTest {

    @Mock
    private CajaRepository cajaRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @InjectMocks
    private CajaServiceImpl cajaService;

    @Test
    @DisplayName("Deberia de crear una caja en la Farmacia")
    void crearCajaExitosa(){

        Long farmaciaId = 1L;

        //ARRANGE
        CajaCreateDTO dto = new  CajaCreateDTO();
        dto.setSucursalId(1L);
        dto.setCajaNombre("Caja 1");

        //Entidades Relacionales
        Sucursal sucursal = new Sucursal();
        sucursal.setSucursalId(1L);

        //Resultado simulado
        Caja caja = new Caja();
        caja.setCajaId(1L);
        caja.setCajaNombre(dto.getCajaNombre());

        //ACC
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        //any, indica que no importa la caja que llegue, solo que la devuelva simulada
        when(cajaRepository.save(any(Caja.class))).thenReturn(caja);

        CajaResponseDTO resultado = cajaService.crearCaja(farmaciaId, dto);

        assertNotNull(resultado);
        assertEquals(dto.getCajaNombre(), resultado.getCajaNombre());

        //Válida que se guardo Correctamente
        ArgumentCaptor<Caja> captor = ArgumentCaptor.forClass(Caja.class);
        verify(cajaRepository).save(captor.capture());
        Caja caja1 = captor.getValue();
    }

    @Test
    @DisplayName("Deberia buscar por texto correctamente")
    void obtenerPorBusquedaTexto(){
        Long farmaciaId = 1L;
        String texto = "Caja 1";
        Pageable pageable = PageRequest.of(0,10);

        Caja caja = new Caja();
        caja.setCajaId(1L);

        Page<Caja> page = new PageImpl<>(List.of(caja));

        //ACC
        when(cajaRepository.buscarPorTexto(texto, pageable)).thenReturn(page);

        //ASSET
        Page<CajaSimpleDTO> resultado = cajaService.buscarPorTexto(farmaciaId, texto, pageable);
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }


}
