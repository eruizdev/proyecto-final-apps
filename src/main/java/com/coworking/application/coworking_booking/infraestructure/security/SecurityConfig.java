package com.coworking.application.coworking_booking.infraestructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad general para el backend.
 * Integra JWT, roles y filtros personalizados.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para API REST
            .authorizeHttpRequests(reg -> reg
                // Swagger y documentación
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/h2/**").permitAll()

                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/workspaces/**").permitAll()

                // Roles protegidos
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/afiliado/**").hasRole("AFILIADO")
                .requestMatchers("/api/visitante/**").hasRole("VISITANTE")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            // Permitir consola H2 (solo para desarrollo)
            .headers(h -> h.frameOptions(f -> f.disable()))

            // Registrar el filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Autenticación simple para el MVP ({noop} = sin cifrado).
     * Puede reemplazarse por BCryptPasswordEncoder en versiones productivas.
     */
    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return new ProviderManager(provider);
    }
}