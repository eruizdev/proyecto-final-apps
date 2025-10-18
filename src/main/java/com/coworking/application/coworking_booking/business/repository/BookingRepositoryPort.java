package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingRepositoryPort {

    
    BookingEntity save(BookingEntity booking);
    Optional<BookingEntity> findById(Long id);

    // Consultas de validación / existencia
    boolean existsOverlap(Long spaceId, LocalDateTime start, LocalDateTime end, Set<BookingEntity.BookingStatus> states);

    // Consultas de conteo y métricas
    int countFutureByUser(Long userId);

    // Consultas de rango de fechas y usuario
    List<BookingEntity> findStartingBetween(LocalDateTime start, LocalDateTime end);
    List<BookingEntity> findByUser(Long userId);
}
