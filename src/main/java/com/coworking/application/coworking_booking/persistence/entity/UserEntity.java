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
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @Column(nullable=false, unique=true, length=255)
	  private String email;

	  @Column(name="password_hash", nullable=false, length=255)
	  private String passwordHash;

	  @Column(name="first_name", nullable=false, length=100)
	  private String firstName;

	  @Column(name="last_name", nullable=false, length=100)
	  private String lastName;

	  @Column(length=20)
	  private String phone;

	  @Enumerated(EnumType.STRING)
	  @Column(name="user_role", nullable=false, length=20)
	  private Role userRole;

	  @Column(nullable=false)
	  private boolean active = true;

	  @Column(name="email_verified", nullable=false)
	  private boolean emailVerified = false;

	  @Column(name="profile_image_url", length=500)
	  private String profileImageUrl;

	  @Column(name="created_at", nullable=false)
	  private LocalDateTime createdAt;

	  @Column(name="updated_at", nullable=false)
	  private LocalDateTime updatedAt;

	  @Column(name="last_login_at")
	  private LocalDateTime lastLoginAt;

	  public enum Role { ADMIN, MANAGER, USER }
}