package com.coworking.application.coworking_booking;

// importes necesarios
import com.coworking.application.coworking_booking.business.exception.ValidationException;
import com.coworking.application.coworking_booking.business.policy.BookingPolicies;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingPoliciesTest {

// Test para validar rango, capacidad y límites de una reserva
 @Test
  void validaRangoYCapacidadYLimites() {
    var space = SpaceEntity.builder().capacity(5).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE).build();
    var start = LocalDateTime.now().plusHours(3);
    var end = start.plusHours(2);
    assertDoesNotThrow(() -> new BookingPolicies().validateCreation(space, start, end, 5, false, 0));
  }

  // Test para verificar que la validación falle cuando la capacidad es insuficiente en una reserva
  @Test
  void fallaPorSolapamiento() {
    var space = SpaceEntity.builder().capacity(5).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE).build();
    var start = LocalDateTime.now().plusHours(3);
    var end = start.plusHours(2);
    assertThrows(ValidationException.class,
        () -> new BookingPolicies().validateCreation(space, start, end, 4, true, 0));
  }

  // Test para verificar que la validacion falle por mas de 30 dias en una reserva
  @Test
  void fallaPorMasDe30Dias() {
    var space = SpaceEntity.builder().capacity(5).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE).build();
    var start = LocalDateTime.now().plusDays(31);
    var end = start.plusHours(1);
    assertThrows(ValidationException.class,
        () -> new BookingPolicies().validateCreation(space, start, end, 4, false, 0));
  }

}