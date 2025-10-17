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
       // Creamos y guardamos un espacio en la base de datos
        var st = types.save(SpaceTypeEntity.builder().name("Sala").basePricePerHour(new BigDecimal("10000"))
        .active(true).createdAt(now).updatedAt(now).build());
        // Crear y guardar un espacio asociado al tipo de espacio
        var s = spaces.save(SpaceEntity.builder().spaceType(st).name("SalaX").capacity(4)
        .pricePerHour(new BigDecimal("12000")).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
        .active(true).createdAt(now).updatedAt(now).build());
    
    // Definir el rango de tiempo para la reserva
    var start = now.plusHours(3);
    var end = start.plusHours(2);
    var booking = bookingService.create(u.getId(), s.getId(), start, end, 3);

    // Verificar que la reserva se haya creado correctamente
    assertNotNull(booking.getId());
    assertEquals(BookingEntity.BookingStatus.CONFIRMED, booking.getBookingStatus());
    assertEquals(new BigDecimal("24000.00"), booking.getTotalAmount());
  }
}