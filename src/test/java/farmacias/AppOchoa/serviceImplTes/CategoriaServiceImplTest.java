package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.model.Categoria;
import farmacias.AppOchoa.repository.CategoriaRepository;
import farmacias.AppOchoa.serviceimpl.CategoriaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    //TEST PARA CREAR CATEGORIA
    //ARRANGE
    @Test
    @DisplayName("Deberia de crear una categoria cuando los datos son validos")
    void crearCategoriaExitosa(){
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("Vitaminas");
        Categoria categoria = Categoria.builder()
                .categoriaId(1L)
                .categoriaNombre("Vitaminas")
                .categoriaEstado(true)
                .build();

        when(categoriaRepository.existsByCategoriaNombre(any())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        //ACT
        CategoriaResponseDTO resultado = categoriaService.crear(dto);
        //ASSERT
        assertNotNull(resultado);
        assertEquals("Vitaminas", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }
}