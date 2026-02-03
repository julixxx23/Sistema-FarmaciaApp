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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Deberia lanzar una presentacion si ya existe una Categoria con ese nombre")
    void crearCategoriaDuplicado(){
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("Suplementos");
        Categoria categoria = Categoria.builder()
                .categoriaId(2L)
                .categoriaNombre("Suplementos")
                .categoriaEstado(true)
                .build();

        when(categoriaRepository.existsByCategoriaNombre(any())).thenReturn(true);
        //ACT y ASSERT
        assertThrows(RuntimeException.class, () ->{
            categoriaService.crear(dto);
        });
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }
}