package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.exception.ValidationException;
import com.coworking.application.coworking_booking.business.policy.BookingPolicies;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingPoliciesTest {

    private final BookingPolicies policies = new BookingPolicies();

    @Test
    void testValidBookingRange() {
        var space = SpaceEntity.builder()
                .capacity(4)
                .active(true)
                .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
                .build();

        assertDoesNotThrow(() ->
                policies.validateCreation(
                        space,
                        LocalDateTime.now().plusHours(2),
                        LocalDateTime.now().plusHours(4),
                        3,
                        false,
                        0
                ));
    }

    @Test
    void testInvalidOverlap() {
        var space = SpaceEntity.builder()
                .capacity(5)
                .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
                .build();

        assertThrows(ValidationException.class, () ->
                policies.validateCreation(
                        space,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        2,
                        true,
                        0
                ));
    }

    @Test
    void testCapacityExceeded() {
        var space = SpaceEntity.builder()
                .capacity(2)
                .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
                .build();

        assertThrows(ValidationException.class, () ->
                policies.validateCreation(
                        space,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        5,
                        false,
                        0
                ));
    }

    @Test
    void testFutureBookingTooFar() {
        var space = SpaceEntity.builder()
                .capacity(3)
                .spaceStatus(SpaceEntity.SpaceStatus.AVAILABLE)
                .build();

        assertThrows(ValidationException.class, () ->
                policies.validateCreation(
                        space,
                        LocalDateTime.now().plusDays(40),
                        LocalDateTime.now().plusDays(40).plusHours(1),
                        1,
                        false,
                        0
                ));
    }
}
