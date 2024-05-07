package com.fraporti.backend.service;

import com.fraporti.backend.model.Permissao;
import com.fraporti.backend.model.Usuario;
import com.fraporti.backend.repository.PermissaoRepository;
import com.fraporti.backend.repository.UsuarioRepository;
import com.fraporti.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PermissaoRepository permissaoRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PermissaoRepository permissaoRepository,
            AuthenticationManager authenticationManager
    ){
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.permissaoRepository = permissaoRepository;
    }

    public ResponseEntity<?> login(Usuario user){
        System.out.println("Login: "+user.getUsername());
        try{
            final Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            System.out.println("auth: "+authentication);
            if(authentication.isAuthenticated()){
                String token = JwtUtil.generateToken(user.getUsername());
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Credenciais inválidas!", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> create(Usuario user){
        Optional<Usuario> usuario = this.usuarioRepository.findByUsername(user.getUsername());
        if(usuario.isPresent()){
            return new ResponseEntity<>("Já existe uma conta cadastrada para esse email!", HttpStatus.UNAUTHORIZED);
        }


        /*
         * Para testes, deve ser implementado de forma melhor, mas assim da pra testar no postman:
         * {
                "username": "teste@gmail.com",
                "password": "admin",
                "authorities": [
                    {"description": "ADMIN"},
                    {"description": "USER"}
                ]
         *  }
         */
        List<Permissao> perms = permissaoRepository.findAll();
        List<Permissao> userPerms = new ArrayList<>();
        for (Permissao p: perms){
            for(Permissao perm: user.getAuthorities()){
                if(p.getDescription().equals(perm.getDescription())){
                    userPerms.add(p);
                }
            }
        }


        user.setLocked(false);
        user.setEnabled(true);
        user.setExpired(false);
        user.setCredentialsExpired(false);
        user.setAuthorities(userPerms);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.usuarioRepository.save(user);
        user.setPassword(null);
        return new ResponseEntity<>("Criado!", HttpStatus.CREATED);
    }
}
