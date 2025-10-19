package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_plans")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SubscriptionPlanEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;                 // idSub

  @Column(nullable = false, unique = true, length = 100)
  private String type;             // tipo de sub (nombre/plan)

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;        // precio de la sub

  @Builder.Default
  private boolean active = true;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PrePersist
  void onCreate() {
    createdAt = updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
