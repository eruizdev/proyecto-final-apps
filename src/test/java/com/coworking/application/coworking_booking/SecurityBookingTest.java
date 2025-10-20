package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceTypeJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.PaymentJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.AuditLogJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.NotificationJpaRepository; // ✅ NUEVO

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

@SpringBootTest
@AutoConfigureMockMvc
class SecurityBookingTest {

  @Autowired MockMvc mvc;
  @Autowired JwtProvider jwt;

  @Autowired UserJpaRepository users;
  @Autowired SpaceTypeJpaRepository types;
  @Autowired SpaceJpaRepository spaces;
  @Autowired BookingJpaRepository bookings;
  @Autowired PaymentJpaRepository payments;
  @Autowired AuditLogJpaRepository audits;
  @Autowired NotificationJpaRepository notifications; // ✅ NUEVO

  Long userId;
  Long spaceId;

  @BeforeEach
  void seed() {
    // 🔥 Limpieza ordenada respetando dependencias y FK
    payments.deleteAll();
    payments.flush();

    bookings.deleteAll();
    bookings.flush();

    spaces.deleteAll();
    spaces.flush();

    types.deleteAll();
    types.flush();

    audits.deleteAll();
    audits.flush();

    notifications.deleteAll(); // ✅ Limpia notificaciones antes de usuarios
    notifications.flush();

    users.deleteAll();
    users.flush();

    // 🧩 Carga de datos base
    var now = LocalDateTime.now();

    var u = users.save(UserEntity.builder()
        .email("sec@demo.com")
        .passwordHash("{noop}x")
        .firstName("Sec")
        .lastName("User")
        .userRole(UserEntity.Role.USER)
        .active(true)
        .emailVerified(true)
        .createdAt(now)
        .updatedAt(now)
        .build());
    userId = u.getId();

    var st = types.save(SpaceTypeEntity.builder()
        .name("Tipo")
        .basePricePerHour(new BigDecimal("10000"))
        .active(true)
        .createdAt(now)
        .updatedAt(now)
        .build());

    var s = spaces.save(SpaceEntity.builder()
        .spaceType(st)
        .name("SecRoom")
        .capacity(4)
        .pricePerHour(new BigDecimal("12000"))
        .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
        .active(true)
        .createdAt(now)
        .updatedAt(now)
        .build());
    spaceId = s.getId();
  }

  @Test
  void sinTokenDa401_conToken201() throws Exception {
    var start = LocalDateTime.now().plusHours(3).withNano(0);
    var end   = start.plusHours(2);
    var body = """
      {"userId": %d, "spaceId": %d, "startTime": "%s", "endTime":"%s", "attendees": 2}
      """.formatted(userId, spaceId, start, end);

    // Sin token → debe dar 403 o 401
    mvc.perform(post("/api/bookings")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isForbidden());

    // Con token válido → debe dar 201
    String token = jwt.generateToken(userId, "sec@demo.com", "USER");

    mvc.perform(post("/api/bookings")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isCreated());
  }
}
