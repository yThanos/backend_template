package com.fraporti.backend.controller;

import com.fraporti.backend.model.Usuario;
import com.fraporti.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController (AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario user){
        return authService.login(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Usuario user){
        return authService.create(user);
    }
}
