package com.coworking.application.coworking_booking.infraestructure.config;

import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

  @Bean
  CommandLineRunner seed(UserJpaRepository users,
                         SpaceTypeJpaRepository types,
                         SpaceJpaRepository spaces,
                         PasswordEncoder passwordEncoder) {
    return args -> {
      var now = LocalDateTime.now();

      // Admin por defecto con contraseña en BCrypt
      users.save(UserEntity.builder()
          .email("admin@demo.com")
          .passwordHash(passwordEncoder.encode("admin"))
          .firstName("Admin")
          .lastName("Demo")
          .userRole(UserEntity.Role.ADMIN)
          .active(true)
          .emailVerified(true)
          .createdAt(now)
          .updatedAt(now)
          .build());

      var st = types.save(SpaceTypeEntity.builder()
          .name("Sala Reuniones")
          .basePricePerHour(new BigDecimal("25000.00"))
          .active(true)
          .createdAt(now)
          .updatedAt(now)
          .build());

      spaces.save(SpaceEntity.builder()
          .spaceType(st)
          .name("Sala 101")
          .capacity(8)
          .pricePerHour(new BigDecimal("30000.00"))
          .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
          .active(true)
          .createdAt(now)
          .updatedAt(now)
          .build());
    };
  }
}
