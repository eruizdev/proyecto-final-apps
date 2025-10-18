package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
  BookingEntity create(Long userId, Long spaceId, LocalDateTime start, LocalDateTime end, int attendees);
  BookingEntity get(Long id);
  void cancel(Long id);
  List<BookingEntity> listByUser(Long userId, int limit);
}
