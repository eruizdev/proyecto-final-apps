package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.*;
import java.time.LocalDateTime;
import java.util.*;

public interface PaymentRepositoryPort {
  PaymentEntity save(PaymentEntity p);
  Optional<PaymentEntity> findByBookingId(Long bookingId);
}
