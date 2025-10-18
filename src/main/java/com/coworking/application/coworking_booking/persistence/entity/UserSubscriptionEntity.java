package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions",
       indexes = @Index(name = "idx_user_active", columnList = "user_id,active"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscriptionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "plan_id")
  private MembershipPlanEntity plan;

  @Column(nullable = false)
  private boolean active = true;

  private LocalDateTime startAt;
  private LocalDateTime endAt;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
