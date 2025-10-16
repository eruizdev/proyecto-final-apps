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

    //  1 Crear usuario de prueba 
    var u = users.save(UserEntity.builder()
        .email("n@a.com")
        .passwordHash("{noop}x")
        .firstName("N")
        .lastName("A")
        .userRole(UserEntity.Role.USER)
        .active(true)
        .emailVerified(true)
        .createdAt(now)
        .updatedAt(now)
        .build());

        // 2 Crear tipo de espacio
    var st = types.save(SpaceTypeEntity.builder()
        .name("Tipo")
        .basePricePerHour(new BigDecimal("10000"))
        .active(true)
        .createdAt(now)
        .updatedAt(now)
        .build());

    // 3 Crear espacio asociado
    var s = spaces.save(SpaceEntity.builder()
        .spaceType(st)
        .name("Sala A")
        .capacity(3)
        .pricePerHour(new BigDecimal("12000"))
        .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
        .active(true)
        .createdAt(now)
        .updatedAt(now)
        .build());

    // 4 Crear reserva
    var start = now.plusHours(3);
    var end = start.plusHours(1);
    var booking = bookingService.create(u.getId(), s.getId(), start, end, 2);

    // 5 Verificar auditoría
    var auditList = audits.findAll();
    assertThat(auditList)
        .as("Debe haberse registrado una acción CREATE en AuditLog")
        .anyMatch(a -> a.getEntityId().equals(booking.getId())
                && "CREATE".equals(a.getAction()));

    
    // 6 Verificar notificación
    var notifList = notifications.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(u.getId());
    assertFalse(notifList.isEmpty(), "Debe existir al menos una notificación no leída");
    assertThat(notifList)
        .as("Debe existir una notificación de confirmación")
        .anyMatch(n -> n.getUser().getId().equals(u.getId())
                && n.getTitle().toLowerCase().contains("confirmada"));
  }
}