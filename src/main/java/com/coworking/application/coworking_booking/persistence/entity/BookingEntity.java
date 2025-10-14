package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="bookings",
  indexes = {
    @Index(name="idx_user_date", columnList="user_id,start_time"),
    @Index(name="idx_space_date", columnList="space_id,start_time,end_time"),
    @Index(name="idx_booking_status", columnList="booking_status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne(optional=false) @JoinColumn(name="user_id")
	  private UserEntity user;

	  @ManyToOne(optional=false) @JoinColumn(name="space_id")
	  private SpaceEntity space;

	  @Column(name="start_time", nullable=false)
	  private LocalDateTime startTime;

	  @Column(name="end_time", nullable=false)
	  private LocalDateTime endTime;

	  @Enumerated(EnumType.STRING)
	  @Column(name="booking_status", nullable=false, length=20)
	  private BookingStatus bookingStatus;

	  @Column(nullable=false)
	  private Integer attendees;

	  @Column(name="total_amount", nullable=false, precision=10, scale=2)
	  private BigDecimal totalAmount;

	  @Column(name="created_at", nullable=false)
	  private LocalDateTime createdAt;

	  @Column(name="updated_at", nullable=false)
	  private LocalDateTime updatedAt;

	  public enum BookingStatus { PENDING, CONFIRMED, CANCELLED }
	
}