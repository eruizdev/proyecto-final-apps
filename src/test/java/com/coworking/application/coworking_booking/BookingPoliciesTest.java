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
<<<<<<< HEAD
        var space = SpaceEntity.builder().capacity(4).active(true).build();
        assertDoesNotThrow(() ->
                policies.validateCreation(space, LocalDateTime.now().plusHours(2),
                        LocalDateTime.now().plusHours(4), 3, false, 0));
=======
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
>>>>>>> 699c8e9 (commit)
    }

    @Test
    void testInvalidOverlap() {
<<<<<<< HEAD
        var space = SpaceEntity.builder().capacity(5).build();
        assertThrows(ValidationException.class, () ->
                policies.validateCreation(space, LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2), 2, true, 0));
=======
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
>>>>>>> 699c8e9 (commit)
    }

    @Test
    void testCapacityExceeded() {
<<<<<<< HEAD
        var space = SpaceEntity.builder().capacity(2).build();
        assertThrows(ValidationException.class, () ->
                policies.validateCreation(space, LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2), 5, false, 0));
=======
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
>>>>>>> 699c8e9 (commit)
    }

    @Test
    void testFutureBookingTooFar() {
<<<<<<< HEAD
        var space = SpaceEntity.builder().capacity(3).build();
        assertThrows(ValidationException.class, () ->
                policies.validateCreation(space, LocalDateTime.now().plusDays(40),
                        LocalDateTime.now().plusDays(40).plusHours(1), 1, false, 0));
=======
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
>>>>>>> 699c8e9 (commit)
    }
}
