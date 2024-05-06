package com.fraporti.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "PERMISSOES")
public class Permissao {
    @Id
    @Column(name = "ID_PERMISSAO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DESCRICAO")
    private String description;

    @ManyToMany(mappedBy = "authorities")
    private List<Usuario> users;

    public Permissao() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Usuario> getUsers() {
        return users;
    }

    public void setUsers(List<Usuario> users) {
        this.users = users;
    }
}
