package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spaces",
  uniqueConstraints = @UniqueConstraint(name = "uk_space_name", columnNames = "name"),
  indexes = {
    @Index(name = "idx_space_type", columnList = "space_type_id"),
    @Index(name = "idx_space_status", columnList = "space_status"),
    @Index(name = "idx_capacity", columnList = "capacity"),
    @Index(name = "idx_price_per_hour", columnList = "price_per_hour"),
    @Index(name = "idx_active_space", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceEntity {
	
	
}

