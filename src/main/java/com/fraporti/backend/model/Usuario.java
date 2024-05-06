package com.fraporti.backend.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "USUARIOS")
public class Usuario {
    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "EMAIL")
    private String username;
    @Column(name = "SENHA")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "USUARIO_PERMISSAO",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISSAO")
    )
    private Collection<Permissao> authorities;

    @Transient
    private String token;

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Permissao> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Permissao> authorities) {
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
