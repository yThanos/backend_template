package com.fraporti.backend.model;

import jakarta.persistence.*;

import java.util.List;

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
    @Column(name = "ATIVO")
    private boolean enabled;
    @Column(name = "EXPIRADO")
    private boolean expired;
    @Column(name = "BLOQUEADO")
    private boolean locked;
    @Column(name = "SENHA_EXPIRADA")
    private boolean credentialsExpired;
    //ToDo: para melhor utilização, é interessante acrescentar campos de validade da senha que "controle" o campo de senha expirada

    @ManyToMany
    @JoinTable(
            name = "USUARIO_PERMISSAO",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISSAO")
    )
    private List<Permissao> authorities;

    @Transient
    private String token;

    public Usuario() {}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

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

    public List<Permissao> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Permissao> authorities) {
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
