package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_fines",
       indexes = {
         @Index(name = "idx_fines_user", columnList = "user_id"),
         @Index(name = "idx_fines_created_at", columnList = "created_at")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserFineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(nullable = false, length = 200)
  private String reason;

  @Column(nullable = false)
  private Integer amount; // precio/monto de la multa

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
