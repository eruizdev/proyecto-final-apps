package com.coworking.application.coworking_booking;

// importes necesarios
import com.coworking.application.coworking_booking.business.service.BookingService;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// Anotaciones para configurar el entorno de pruebas
@SpringBootTest
class NotificationAuditIntegrationTest {

    // Inyección de dependencias necesarias para las pruebas
  @Autowired BookingService bookingService;
  @Autowired UserJpaRepository users;
  @Autowired SpaceTypeJpaRepository types;
  @Autowired SpaceJpaRepository spaces;
  @Autowired NotificationJpaRepository notifications;
  @Autowired AuditLogJpaRepository audits;

  // Test para verificar la creación de notificaciones y registros de auditoría
  @Test
  void alCrearReservaSeGeneranNotificacionYAuditoria() {
    var now = LocalDateTime.now();



  }
}