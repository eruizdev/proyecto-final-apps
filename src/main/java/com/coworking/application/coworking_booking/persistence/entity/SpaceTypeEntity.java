package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "space_types",
  indexes = {
    @Index(name = "idx_space_types_active", columnList = "active"),
    @Index(name = "idx_space_types_base_price_per_hour", columnList = "base_price_per_hour")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpaceTypeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "base_price_per_hour", nullable = false, precision = 10, scale = 2)
  private BigDecimal basePricePerHour;

  @Column(columnDefinition = "TEXT")
  private String amenities;

  @Column(nullable = false)
  private Boolean active = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
