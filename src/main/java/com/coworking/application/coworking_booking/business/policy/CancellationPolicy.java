package com.coworking.application.coworking_booking.business.policy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

public class CancellationPolicy {
  public BigDecimal computeRefund(BigDecimal paidAmount, LocalDateTime start, LocalDateTime now){
    if (Duration.between(now, start).toHours() < 24) {
      return paidAmount.multiply(new BigDecimal("0.50")); // 50% reembolso
    }
    return paidAmount;
  }
}
