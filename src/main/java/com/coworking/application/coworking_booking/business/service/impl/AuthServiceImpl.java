package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepositoryPort users;
  private final JwtProvider jwt;
  private final UserJpaRepository userJpa;
  private final PasswordEncoder passwordEncoder;

  // Constructor principal (con PasswordEncoder inyectable)
  public AuthServiceImpl(UserRepositoryPort users,
                         JwtProvider jwt,
                         UserJpaRepository userJpa,
                         PasswordEncoder passwordEncoder) {
    this.users = users;
    this.jwt = jwt;
    this.userJpa = userJpa;
    this.passwordEncoder = passwordEncoder;
  }

  // Constructor de compatibilidad para tus tests existentes (3 args)
  // Crea un BCryptPasswordEncoder por defecto.
  public AuthServiceImpl(UserRepositoryPort users,
                         JwtProvider jwt,
                         UserJpaRepository userJpa) {
    this(users, jwt, userJpa, new BCryptPasswordEncoder());
  }

  @Override
  public UserEntity register(String email, String pass, String first, String last) {
    var now = LocalDateTime.now();
    var u = UserEntity.builder()
        .email(email)
        // Guardar con BCRYPT (reemplazo del {noop})
        .passwordHash(passwordEncoder.encode(pass))
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

  @Override
  public UserEntity get(Long id) {
    return users.require(id);
  }

  @Override
  public String login(String email, String password) {
    var u = userJpa.findByEmail(email)
        .orElseGet(() -> users.findAll().stream()
            .filter(x -> x.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas")));

    if (!matchesLegacyAware(password, u.getPasswordHash())) {
      throw new RuntimeException("Credenciales inválidas");
    }

    return jwt.generateToken(
        u.getId(),
        u.getEmail(),
        u.getUserRole().name()
    );
  }

  /**
   * Valida la contraseña soportando hashes antiguos con {noop} temporalmente.
   * - Si el hash comienza con {noop}, compara en texto plano.
   * - Si no, usa el PasswordEncoder (BCrypt).
   */
  private boolean matchesLegacyAware(String rawPassword, String storedHash) {
    if (storedHash != null && storedHash.startsWith("{noop}")) {
      return ("{noop}" + rawPassword).equals(storedHash);
    }
    return passwordEncoder.matches(rawPassword, storedHash);
  }
}
