package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="payments",
  indexes = {
    @Index(name="idx_payment_status", columnList="payment_status"),
    @Index(name="idx_payment_date", columnList="payment_date")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentEntity {
	
	
}