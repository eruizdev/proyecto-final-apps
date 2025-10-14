package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "space_types",
        indexes = {
                @Index(name = "idx_active", columnList = "active"),
                @Index(name = "idx_base_price_per_hour", columnList = "base_price_per_hour")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceTypeEntity {
	
	
}