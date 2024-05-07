package com.fraporti.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping("/user")
    public String user(){
        return "Conta com permissão de USER!";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Conta com permissão de ADMIN!";
    }

    @GetMapping("/both")
    public String both(){
        return "Conta com permissão de USER ou ADMIN!";
    }
}
