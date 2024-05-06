package com.fraporti.backend.service;

import com.fraporti.backend.model.Usuario;
import com.fraporti.backend.repository.UsuarioRepository;
import com.fraporti.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager){
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> login(Usuario user){
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(authentication.isAuthenticated()){
            String token = JwtUtil.generateToken(user.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Credenciais inválidas!", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> create(Usuario user){
        Optional<Usuario> usuario = this.usuarioRepository.findByUsername(user.getUsername());
        if(usuario.isPresent()){
            return new ResponseEntity<>("Já existe uma conta cadastrada para esse email!", HttpStatus.UNAUTHORIZED);
        }
        user = this.usuarioRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
