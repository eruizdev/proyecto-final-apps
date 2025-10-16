package com.coworking.application.coworking_booking;

// importaciones necesarias
import com.coworking.application.coworking_booking.business.service.BookingService;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Anotación para indicar que es una prueba de integración de Spring Boot
@SpringBootTest
class BookingServiceIntegrationTest {

// Prueba para verificar la creación de una reserva
// y anotaciones necesarias de JUnit
  @Autowired BookingService bookingService;
  @Autowired UserJpaRepository users;
  @Autowired SpaceTypeJpaRepository types;
  @Autowired SpaceJpaRepository spaces;

  // Prueba para crear una reserva confirmada con pago
  @Test
  void creaReservaConfirmadaConPago() {
    var now = LocalDateTime.now();
    // Crear y guardar un tipo de espacio
    var u = users.save(UserEntity.builder().email("t@t.com").passwordHash("{noop}t")
        .firstName("Test").lastName("User").userRole(UserEntity.Role.USER)
        .active(true).emailVerified(true).createdAt(now).updatedAt(now).build());
  }
}