package com.fraporti.backend.security;

import com.fraporti.backend.model.Usuario;
import com.fraporti.backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if(usuario.isPresent()){
            Usuario user = usuario.get();

            UserDetailsImpl details = new UserDetailsImpl();
            details.setUsername(user.getUsername());
            details.setPassword(user.getPassword());
            details.setAuthorities(user.getAuthorities().stream().map(permissao ->
                    new AuthorityImpl(permissao.getDescription())).collect(Collectors.toList()));

            return details;
        }
        throw new UsernameNotFoundException("Usuario não encontrado");
    }
}
