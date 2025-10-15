package com.coworking.application.coworking_booking.infraestructure.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT que valida el token enviado en el encabezado Authorization.
 * Extrae las claims (sub, email, role) y las asigna al contexto de seguridad.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            try {
                // Parsear y validar token
                Claims claims = jwtProvider.parse(header.substring(7)).getBody();

                // Extraer información del token
                String subject = claims.getSubject(); // normalmente userId o email
                String email = (String) claims.get("email");
                String role = (String) claims.get("role");

                // Construir autoridad con prefijo "ROLE_"
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // Autenticación del contexto
                Authentication authToken = new UsernamePasswordAuthenticationToken(
                        email != null ? email : subject,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception ignored) {
                // Si el token es inválido o expiró, no se lanza excepción
                SecurityContextHolder.clearContext();
            }
        }

        // Continuar la cadena de filtros
        chain.doFilter(req, res);
    }	
}