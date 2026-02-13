package farmacias.AppOchoa.services;

import farmacias.AppOchoa.model.Usuario;
import farmacias.AppOchoa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con: " + username));

        return new User(
                usuario.getNombreUsuarioUsuario(),
                usuario.getUsuarioContrasenaHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getUsuarioRol().name().toUpperCase()))
        );
    }
}