package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.BookingRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepositoryPort {
	
	
}


