package farmacias.AppOchoa.serviceImplTes;

import farmacias.AppOchoa.dto.usuario.UsuarioCreateDTO;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.serviceimpl.UsuarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {
    @Mock
    private SucursalRepository sucursalRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    //TEST PARA CREAR USUARIO
    @Test
    @DisplayName("Deberia crear un usuario cuando los datos son validos")
    void crearUsuario_Exito(){
        return;
    }
    UsuarioCreateDTO dto = new UsuarioCreateDTO();
    dto
}
