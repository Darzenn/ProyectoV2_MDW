package com.example.ProyectoV2_MDW.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class Security {
    

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/index", "/zonaRegistro", "/productos", "/login", "/logout", "/css/**").permitAll()
                // Cualquier otra requiere login
                .anyRequest().authenticated()
)
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll());

        return http.build();
}

}
