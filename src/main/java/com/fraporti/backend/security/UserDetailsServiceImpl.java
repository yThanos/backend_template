package com.fraporti.backend.security;

import com.fraporti.backend.model.Permissao;
import com.fraporti.backend.model.Usuario;
import com.fraporti.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if(usuario.isPresent()){
            Usuario user = usuario.get();

            UserDetailsImpl details = new UserDetailsImpl();
            details.setUsername(user.getUsername());
            details.setPassword(user.getPassword());
            details.setAuthorities(AuthorityUtils.createAuthorityList(
                    user.getAuthorities()
                            .stream()
                            .map(Permissao::getDescription)
                            .toArray(String[]::new)
            ));
            details.setEnabled(user.isEnabled());
            details.setAccountNonExpired(!user.isExpired());
            details.setAccountNonLocked(!user.isLocked());
            details.setCredentialsNonExpired(!user.isCredentialsExpired());



            return details;
        }
        throw new UsernameNotFoundException("Usuario não encontrado");
    }
}
