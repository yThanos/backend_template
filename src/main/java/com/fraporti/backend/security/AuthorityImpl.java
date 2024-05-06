package com.fraporti.backend.security;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityImpl implements GrantedAuthority {
    private String authorities;

    public AuthorityImpl() {}

    public AuthorityImpl(String authorities){
        this.authorities = authorities;
    }

    @Override
    public String getAuthority() {
        return authorities;
    }

    public void setAuthorities(String authorities){
        this.authorities = authorities;
    }
}
