package com.coworking.application.coworking_booking;

// importes necesarios
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Anotaciones para configurar el entorno de pruebas
@DataJpaTest
class BookingRepositoryOverlapTest {

// Anotaciones para inyectar los repositorios necesarios
  @Autowired BookingJpaRepository bookings;
  @Autowired UserJpaRepository users;
  @Autowired SpaceJpaRepository spaces;
  @Autowired SpaceTypeJpaRepository types;

  // Test para detectar solapamiento en reservas de la misma sala
  @Test
  void detectaSolapamientoEnMismaSala() {
    var now = LocalDateTime.now();
    // Crear usuario de prueba
    var u = users.save(UserEntity.builder().email("u@x.com").passwordHash("x")
        .firstName("U").lastName("X").userRole(UserEntity.Role.USER)
        .active(true).emailVerified(true).createdAt(now).updatedAt(now).build());
    // Crear tipo de espacio
    var st = types.save(SpaceTypeEntity.builder().name("Tipo").basePricePerHour(new BigDecimal("1"))
        .active(true).createdAt(now).updatedAt(now).build());
    
  }
}