package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name="users",
  indexes = {
    @Index(name="idx_email", columnList="email", unique = true),
    @Index(name="idx_user_role", columnList="user_role"),
    @Index(name="idx_active", columnList="active")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {
	
	
}