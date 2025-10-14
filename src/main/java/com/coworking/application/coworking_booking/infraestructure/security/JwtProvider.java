package com.coworking.application.coworking_booking.infraestructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
	
	private final Key key;
    private final long expMillis;

    /**
     * Constructor para inyectar valores desde application.properties o application.yml
     * Ejemplo:
     * security.jwt.secret=super-secret-demo-key-32bytes-minimo123456
     * security.jwt.exp-min=360
     */
    public JwtProvider(@Value("${security.jwt.secret}") String secret,
                       @Value("${security.jwt.exp-min}") long expMin) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expMillis = expMin * 60_000; // convierte minutos en milisegundos
    }

    /**
     * Genera un token JWT con claims personalizados.
     *
     * @param userId ID del usuario (se guarda como subject)
     * @param email  correo del usuario
     * @param role   rol del usuario
     * @return Token JWT firmado (HS256)
     */
    public String generateToken(Long userId, String email, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(new Date(System.currentTimeMillis() + expMillis))
                .addClaims(Map.of("email", email, "role", role))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * También puedes generar un token genérico usando un mapa de claims.
     */
    public String generate(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida y parsea un JWT devolviendo sus Claims.
     */
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}