package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import java.util.List;

public interface AuditLogRepositoryPort {
  AuditLogEntity save(AuditLogEntity log);
  List<AuditLogEntity> listAll();
  List<AuditLogEntity> listByUser(Long userId);
  List<AuditLogEntity> listByEntityType(String entityType, int limit);
}
