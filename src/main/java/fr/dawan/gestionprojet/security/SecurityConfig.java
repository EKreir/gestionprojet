package fr.dawan.gestionprojet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration principale de la sécurité.
     * Pour le développement : on autorise tout (aucune authentification requise).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF (sinon bloque les POST/PUT venant de Postman)
                .csrf(csrf -> csrf.disable())

                // Autorise toutes les requêtes sans authentification
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // toutes les routes /api/
                        .anyRequest().permitAll() // le reste aussi
                )

                // Désactive les formulaires et la sécurité HTTP Basic
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

}