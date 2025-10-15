package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
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
}