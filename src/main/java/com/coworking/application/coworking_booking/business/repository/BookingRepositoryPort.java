package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface BookingRepositoryPort {
    BookingEntity save(BookingEntity b);
    Optional<BookingEntity> findById(Long id);
    boolean existsOverlap(Long spaceId, LocalDateTime start, LocalDateTime end, Set<BookingEntity.BookingStatus> states);
    int countFutureByUser(Long userId);
}


