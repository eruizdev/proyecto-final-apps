package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.policy.BookingPolicies;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingPoliciesExtraTest {

    private final BookingPolicies policies = new BookingPolicies();

    @Test
    void testNegativeDurationThrows() {
        var s = SpaceEntity.builder().capacity(3).build();
        var now = LocalDateTime.now();
        assertThrows(Exception.class, () -> policies.validateCreation(s, now.plusHours(3), now.plusHours(1), 2, false, 0));
    }

    @Test
    void testNullSpaceThrows() {
        assertThrows(NullPointerException.class, () ->
                policies.validateCreation(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1, false, 0));
    }

    @Test
    void testZeroAttendeesValid() {
        var s = SpaceEntity.builder().capacity(5).build();
        assertDoesNotThrow(() ->
                policies.validateCreation(s, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), 0, false, 0));
    }

    @Test
    void testFutureLimit30DaysOk() {
        var s = SpaceEntity.builder().capacity(5).build();
        assertDoesNotThrow(() ->
                policies.validateCreation(s, LocalDateTime.now().plusDays(29), LocalDateTime.now().plusDays(29).plusHours(2), 2, false, 0));
    }
}
