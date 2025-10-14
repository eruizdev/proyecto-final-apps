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
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @OneToOne(optional=false) @JoinColumn(name="booking_id", unique = true)
	  private BookingEntity booking;

	  @Column(nullable=false, precision=10, scale=2)
	  private BigDecimal amount;

	  @Enumerated(EnumType.STRING)
	  @Column(name="payment_status", nullable=false, length=20)
	  private PaymentStatus paymentStatus;

	  @Column(name="payment_method", length=50)
	  private String paymentMethod;

	  @Column(name="transaction_id", length=100)
	  private String transactionId;

	  @Column(name="gateway_response", columnDefinition="TEXT")
	  private String gatewayResponse;

	  @Column(name="payment_date")
	  private LocalDateTime paymentDate;

	  @Column(name="created_at", nullable=false)
	  private LocalDateTime createdAt;

	  @Column(name="updated_at", nullable=false)
	  private LocalDateTime updatedAt;

	  public enum PaymentStatus { PENDING, PROCESSING, PAID, FAILED, REFUNDED }
	
}