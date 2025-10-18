package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
  PaymentEntity findByBookingId(Long bookingId);
  List<PaymentEntity> findByBooking_User_IdOrderByPaymentDateDesc(Long userId);
}
