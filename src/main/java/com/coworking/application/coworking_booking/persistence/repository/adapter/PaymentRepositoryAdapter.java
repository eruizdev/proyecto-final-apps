package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.*;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component @RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {
	
	private final PaymentJpaRepository jpa;
	  public PaymentEntity save(PaymentEntity p){ return jpa.save(p); }
	  public Optional<PaymentEntity> findByBookingId(Long bookingId){
	    return Optional.ofNullable(jpa.findByBookingId(bookingId));
	  }
}