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
	
	
}