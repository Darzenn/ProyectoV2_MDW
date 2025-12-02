package com.example.ProyectoV2_MDW.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/", "/index","/carrito/**", "/perfil/**", "/registro", "/productos", "/login", "/logout").permitAll()
                        // Cualquier otra requiere login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/registro")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll());

        return http.build();
    }

}
