package com.fraporti.backend.security;

import com.fraporti.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class AuthFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDatails;

    public AuthFilter(UserDetailsServiceImpl customUserDatails) {
        this.userDatails = customUserDatails;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String username = JwtUtil.getUsernameToken(authorization);
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("/auth")) {
            System.out.println("Login");
            filterChain.doFilter(request, response);
            return;
        }
        if (authorization == null) {
            System.out.println("Sem token");
            response.sendError(401);
            return;
        } else if(JwtUtil.isTokenExpired(authorization)){
            System.out.println("Token expirado");
            response.sendError(419);
            return;
        } else {
            System.out.println("Chegou no context holder...");
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails user = userDatails.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        System.out.println("Passou o filtro!");
        filterChain.doFilter(request, response);
    }
}
