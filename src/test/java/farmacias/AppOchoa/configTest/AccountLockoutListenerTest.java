package farmacias.AppOchoa.configTest;

import farmacias.AppOchoa.config.AccountLockoutListener;
import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountLockoutListener (M2)")
public class AccountLockoutListenerTest {

    private static final String USERNAME = "cajero1";
    private static final int MAX_INTENTOS = 5;

    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private AccountLockoutListener listener;

    private Usuario usuarioConIntentos(int intentos, LocalDateTime bloqueadoHasta) {
        Usuario u = new Usuario();
        u.setNombreUsuarioUsuario(USERNAME);
        u.setUsuarioIntentosFallidos(intentos);
        u.setUsuarioBloqueadoHasta(bloqueadoHasta);
        return u;
    }

    private AuthenticationFailureBadCredentialsEvent fallo(String username) {
        Authentication auth = new UsernamePasswordAuthenticationToken(username, "wrong");
        return new AuthenticationFailureBadCredentialsEvent(
                auth, new org.springframework.security.authentication.BadCredentialsException("bad"));
    }

    @Test
    @DisplayName("Un fallo incrementa el contador sin bloquear")
    void falloIncrementaContador() {
        Usuario u = usuarioConIntentos(0, null);
        when(usuarioRepository.findByNombreUsuarioUsuario(USERNAME)).thenReturn(Optional.of(u));

        listener.onFailure(fallo(USERNAME));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getUsuarioIntentosFallidos());
        assertNull(captor.getValue().getUsuarioBloqueadoHasta());
    }

    @Test
    @DisplayName("El quinto fallo bloquea la cuenta ~15 minutos")
    void quintoFalloBloquea() {
        Usuario u = usuarioConIntentos(MAX_INTENTOS - 1, null);
        when(usuarioRepository.findByNombreUsuarioUsuario(USERNAME)).thenReturn(Optional.of(u));

        LocalDateTime antes = LocalDateTime.now();
        listener.onFailure(fallo(USERNAME));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();
        assertEquals(MAX_INTENTOS, guardado.getUsuarioIntentosFallidos());
        assertNotNull(guardado.getUsuarioBloqueadoHasta());
        assertTrue(guardado.getUsuarioBloqueadoHasta().isAfter(antes.plusMinutes(14)));
        // isAccountNonLocked debe reflejar el bloqueo
        assertFalse(guardado.isAccountNonLocked());
    }

    @Test
    @DisplayName("Tras un bloqueo expirado, el siguiente fallo reinicia el conteo en 1")
    void bloqueoExpiradoReiniciaConteo() {
        Usuario u = usuarioConIntentos(MAX_INTENTOS, LocalDateTime.now().minusMinutes(1));
        when(usuarioRepository.findByNombreUsuarioUsuario(USERNAME)).thenReturn(Optional.of(u));

        listener.onFailure(fallo(USERNAME));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getUsuarioIntentosFallidos());
        assertNull(captor.getValue().getUsuarioBloqueadoHasta());
    }

    @Test
    @DisplayName("Usuario inexistente no provoca guardado (no revela existencia)")
    void usuarioInexistenteNoGuarda() {
        when(usuarioRepository.findByNombreUsuarioUsuario("fantasma")).thenReturn(Optional.empty());

        listener.onFailure(fallo("fantasma"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login exitoso resetea contador y bloqueo")
    void exitoResetea() {
        Usuario principal = usuarioConIntentos(3, null);
        Usuario enBd = usuarioConIntentos(3, LocalDateTime.now().plusMinutes(10));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, java.util.Collections.emptyList());
        when(usuarioRepository.findByNombreUsuarioUsuario(USERNAME)).thenReturn(Optional.of(enBd));

        listener.onSuccess(new AuthenticationSuccessEvent(auth));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals(0, captor.getValue().getUsuarioIntentosFallidos());
        assertNull(captor.getValue().getUsuarioBloqueadoHasta());
    }

    @Test
    @DisplayName("Login exitoso sin intentos previos no escribe en BD")
    void exitoSinIntentosNoGuarda() {
        Usuario principal = usuarioConIntentos(0, null);
        Usuario enBd = usuarioConIntentos(0, null);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, java.util.Collections.emptyList());
        when(usuarioRepository.findByNombreUsuarioUsuario(USERNAME)).thenReturn(Optional.of(enBd));

        listener.onSuccess(new AuthenticationSuccessEvent(auth));

        verify(usuarioRepository, never()).save(any());
    }
}
