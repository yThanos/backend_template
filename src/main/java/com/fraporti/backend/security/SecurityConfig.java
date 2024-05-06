package com.fraporti.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AuthFilter authFilter;
    private final ThrottleFilter throttleFilter;
    private final UserDetailsServiceImpl userDatailsService;

    public SecurityConfig(
            AuthFilter authFilter,
            ThrottleFilter throttleFilter,
            UserDetailsServiceImpl userDatailsService
    ){
        this.authFilter = authFilter;
        this.throttleFilter = throttleFilter;
        this.userDatailsService = userDatailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(this.userDatailsService).passwordEncoder(this.passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(this.throttleFilter, DisableEncodeUrlFilter.class);
        http.addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors(cors->cors.configurationSource(corsConfig->{
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("**");
                    corsConfiguration.addAllowedMethod("**");
                    return corsConfiguration;
                })).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/teste/user").hasAnyAuthority("USER")
                        .requestMatchers("/teste/admin").hasAnyAuthority("ADMIN")
                        .requestMatchers("/teste/both").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
