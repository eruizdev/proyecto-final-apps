package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort users;
    private final JwtProvider jwt; // inyectamos JwtProvider para generar el token
    private final UserJpaRepository userJpa; // 🔹 añadido

    //registro usuarios
    @Override
    public UserEntity register(String email, String pass, String first, String last) {
        var now = LocalDateTime.now();
        var u = UserEntity.builder()
                .email(email)
                .passwordHash("{noop}" + pass) // MVP: sin encriptar (usado con {noop} para Spring Security)
                .firstName(first)
                .lastName(last)
                .userRole(UserEntity.Role.USER)
                .active(true)
                .emailVerified(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return users.save(u);
    }

    //obtencion de usuarios
    @Override
    public UserEntity get(Long id) {
        return users.require(id);
    }

    /**
     * Valida las credenciales básicas del usuario y genera un JWT.
     * Mantiene la lógica original, pero añade generación real del token.
     */
    @Override
    public String login(String email, String password) {
        // 🔹 intenta primero por userJpa si existe (nuevo)
        var u = userJpa.findByEmail(email)
                .orElseGet(() -> users.findAll().stream()
                        .filter(x -> x.getEmail().equalsIgnoreCase(email))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Credenciales inválidas")));

        // Validar contraseña (en este MVP sin encriptar)
        if (!u.getPasswordHash().equals("{noop}" + password)) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwt.generateToken(
                u.getId(),
                u.getEmail(),
                u.getUserRole().name()
        );
    }
}
