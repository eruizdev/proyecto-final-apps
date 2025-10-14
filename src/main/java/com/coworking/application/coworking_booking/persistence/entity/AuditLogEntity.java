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
	
}