package com.coworking.application.coworking_booking;

// importes necesarios
import com.coworking.application.coworking_booking.business.exception.ValidationException;
import com.coworking.application.coworking_booking.business.policy.BookingPolicies;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingPoliciesTest {

// Test para validar rango, capacidad y límites
 @Test
  void validaRangoYCapacidadYLimites() {
    var space = SpaceEntity.builder().capacity(5).spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE).build();
    var start = LocalDateTime.now().plusHours(3);
    var end = start.plusHours(2);
    assertDoesNotThrow(() -> new BookingPolicies().validateCreation(space, start, end, 5, false, 0));
  }

}