package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.*;

import java.time.LocalDateTime;

public interface BookingService {
  BookingEntity create(Long userId, Long spaceId, LocalDateTime start, LocalDateTime end, int attendees);
  BookingEntity get(Long id);
  void cancel(Long bookingId);
}

