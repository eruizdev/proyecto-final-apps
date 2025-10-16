package com.coworking.application.coworking_booking;

// importaciones necesarias
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Anotación para indicar que es una prueba de integración de Spring Boot
@SpringBootTest @AutoConfigureMockMvc
class SecurityBookingTest {

// Prueba para verificar la creación de una reserva
// y anotaciones necesarias de JUnit
  @Autowired MockMvc mvc;
  @Autowired JwtProvider jwt;
  @Autowired UserJpaRepository users;
  @Autowired SpaceTypeJpaRepository types;
  @Autowired SpaceJpaRepository spaces;

  Long userId; Long spaceId;

  // Prueba para crear una reserva confirmada con pago
  @BeforeEach
  void seed(){
    // Crear y guardar un usuario en la base de datos
    var now = LocalDateTime.now();
    // Crear y guardar un tipo de espacio
    var u = users.save(UserEntity.builder().email("sec@demo.com").passwordHash("{noop}x")
        .firstName("Sec").lastName("User").userRole(UserEntity.Role.USER)
        .active(true).emailVerified(true).createdAt(now).updatedAt(now).build());
    // Guardamos el ID del usuario para usarlo en las pruebas
    userId = u.getId();
    var st = types.save(SpaceTypeEntity.builder().name("Tipo").basePricePerHour(new BigDecimal("10000"))
        .active(true).createdAt(now).updatedAt(now).build());
    // Crear y guardar un espacio asociado al tipo de espacio de la reserva
    var s = spaces.save(SpaceEntity.builder().spaceType(st).name("SecRoom").capacity(4)
        .pricePerHour(new BigDecimal("12000")).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
        .active(true).createdAt(now).updatedAt(now).build());
    spaceId = s.getId();
  }

  // Prueba para verificar que sin token se obtiene 401 y con token 201
  @Test
  void sinTokenDa401_conToken201() throws Exception {
    var start = LocalDateTime.now().plusHours(3).withNano(0);
    var end   = start.plusHours(2);
    var body = """
      {"userId": %d, "spaceId": %d, "startTime": "%s", "endTime":"%s", "attendees": 2}
      """.formatted(userId, spaceId, start, end);

  }

}