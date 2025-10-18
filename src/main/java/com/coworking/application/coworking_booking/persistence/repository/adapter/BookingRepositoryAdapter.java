package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.BookingRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingJpaRepository jpa;

    @Override
    public BookingEntity save(BookingEntity b) {
        return jpa.save(b);
    }

    @Override
    public Optional<BookingEntity> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public boolean existsOverlap(Long sid, LocalDateTime s, LocalDateTime e, Set<BookingEntity.BookingStatus> states) {
        return jpa.existsOverlap(sid, s, e, states);
    }

    @Override
    public int countFutureByUser(Long userId) {
        return jpa.countByUserIdAndStartTimeAfter(userId, LocalDateTime.now());
    }

    
    @Override
    public List<BookingEntity> findStartingBetween(LocalDateTime start, LocalDateTime end) {
        return jpa.findByStartTimeBetweenAndBookingStatusIn(
                start,
                end,
                Set.of(
                        BookingEntity.BookingStatus.CONFIRMED,
                        BookingEntity.BookingStatus.PENDING
                )
        );
    }

    @Override
    public List<BookingEntity> findByUser(Long userId) {
        return jpa.findByUserIdOrderByStartTimeDesc(userId);
    }
}
