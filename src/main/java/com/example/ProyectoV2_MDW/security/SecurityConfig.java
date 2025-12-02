package com.example.ProyectoV2_MDW.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/", "/index","/carrito/**", "/perfil/**", "/registro", "/productos", "/login", "/logout", "/autenticar").permitAll()
                        // Cualquier otra requiere login
                        .anyRequest().authenticated()
                )
                //.formLogin(form -> form
                       // .loginPage("/registro")
                       // .permitAll()
                .formLogin(form -> form.disable()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll());

        return http.build();
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
