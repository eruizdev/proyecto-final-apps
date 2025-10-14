package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Set;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {
	
	
}