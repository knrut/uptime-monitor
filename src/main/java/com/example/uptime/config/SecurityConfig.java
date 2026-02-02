package com.example.uptime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // React działa na innym porcie -> CORS
                .cors(cors -> cors.configurationSource(req -> {
                    CorsConfiguration c = new CorsConfiguration();
                    c.setAllowedOrigins(List.of("http://localhost:5173"));
                    c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    c.setAllowedHeaders(List.of("*"));
                    c.setAllowCredentials(true); // kluczowe dla cookies/sesji
                    return c;
                }))
                // na start najprościej wyłączyć CSRF (później wrócimy i zrobimy porządnie)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // pozwól na endpoint logowania i sprawdzenie sesji
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // nie chcemy html login page, tylko endpointy
                .httpBasic(b -> b.disable())
                .formLogin(f -> f.disable())
                .build();
    }
}

