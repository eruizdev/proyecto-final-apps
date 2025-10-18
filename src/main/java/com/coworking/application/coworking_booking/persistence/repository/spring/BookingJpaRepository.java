package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM BookingEntity b
        WHERE b.space.id = :spaceId
          AND b.bookingStatus IN :states
          AND (b.startTime < :end AND b.endTime > :start)
    """)
    boolean existsOverlap(@Param("spaceId") Long spaceId,
                          @Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end,
                          @Param("states") Set<BookingEntity.BookingStatus> states);

    int countByUserIdAndStartTimeAfter(Long userId, LocalDateTime startAfter);

   
    List<BookingEntity> findByStartTimeBetweenAndBookingStatusIn(
            LocalDateTime start,
            LocalDateTime end,
            Set<BookingEntity.BookingStatus> statuses);

    List<BookingEntity> findByUserIdOrderByStartTimeDesc(Long userId);

    @Query("""
        SELECT COUNT(b) FROM BookingEntity b
        WHERE b.startTime BETWEEN :from AND :to
          AND (:spaceId IS NULL OR b.space.id = :spaceId)
    """)
    long countBookings(@Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to,
                       @Param("spaceId") Long spaceId);

    @Query("""
        SELECT COALESCE(SUM(b.totalAmount), 0) FROM BookingEntity b
        WHERE b.startTime BETWEEN :from AND :to
    """)
    java.math.BigDecimal sumRevenue(@Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);
}
