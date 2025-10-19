package com.coworking.application.coworking_booking.infraestructure.config;

import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = true)
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserJpaRepository users,
                           SpaceTypeJpaRepository types,
                           SpaceJpaRepository spaces,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            var now = LocalDateTime.now();

            // Verifica si el admin ya existe para evitar duplicados
            if (users.findByEmail("admin@demo.com").isPresent()) {
                return; // ya hay datos, no hace nada
            }

            // Admin por defecto
            var admin = UserEntity.builder()
                    .email("admin@demo.com")
                    .passwordHash(passwordEncoder.encode("admin"))
                    .firstName("Admin")
                    .lastName("Demo")
                    .userRole(UserEntity.Role.ADMIN)
                    .active(true)
                    .emailVerified(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            users.save(admin);

            // Tipo de espacio
            var st = types.save(SpaceTypeEntity.builder()
                    .name("Sala Reuniones")
                    .basePricePerHour(new BigDecimal("25000.00"))
                    .active(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build());

            // Espacio por defecto
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
