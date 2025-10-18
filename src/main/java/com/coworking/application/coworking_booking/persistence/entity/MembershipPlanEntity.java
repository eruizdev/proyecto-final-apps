package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="membership_plans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MembershipPlanEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=80)
  private String name;

  @Column(nullable=false, precision=10, scale=2)
  private BigDecimal priceMonth;

  @Column(columnDefinition="TEXT")
  private String benefits;

  @Column(nullable=false)
  private boolean active = true;

  @Column(nullable=false)
  private LocalDateTime createdAt;

  @Column(nullable=false)
  private LocalDateTime updatedAt;
}
