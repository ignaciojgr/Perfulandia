package com.perfunlandia.ms_auth_bs.config; // o tu paquete

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No crear sesiones
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/internal/**").permitAll() // Permitir todas las peticiones a /internal/**
                .requestMatchers("/actuator/**").permitAll() // Permitir health checks
                .anyRequest().permitAll() // ← CAMBIO: Permitir todo temporalmente para debug
            );
        // Si no tienes otros endpoints o no quieres el login por defecto de Spring Security,
        // podrías incluso simplificarlo más o configurar .httpBasic(Customizer.withDefaults()); o .formLogin(...) si lo necesitaras.
        // Para APIs JWT, a menudo no se usa httpBasic o formLogin así.
        return http.build();
    }
}
