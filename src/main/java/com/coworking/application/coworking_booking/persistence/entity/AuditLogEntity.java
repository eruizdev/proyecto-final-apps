package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="audit_logs",
  indexes = {
    @Index(name="idx_user_action", columnList="user_id,action"),
    @Index(name="idx_entity", columnList="entity_type,entity_id"),
    @Index(name="idx_created_at_audit", columnList="created_at")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLogEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne(optional=false) @JoinColumn(name="user_id")
	  private UserEntity user;

	  @Column(name="entity_type", nullable=false, length=40)
	  private String entityType;

	  @Column(name="entity_id", nullable=false)
	  private Long entityId;

	  @Column(nullable=false, length=60)
	  private String action;

	  @Column(name="old_values", columnDefinition="TEXT")
	  private String oldValues;

	  @Column(name="new_values", columnDefinition="TEXT")
	  private String newValues;

	  @Column(name="ip_address", length=45)
	  private String ipAddress;

	  @Column(name="user_agent", length=200)
	  private String userAgent;

	  @Column(name="created_at", nullable=false)
	  private LocalDateTime createdAt;
}