package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import java.util.List;

public interface AdminService {
  List<AuditLogEntity> auditsAll();
  List<AuditLogEntity> auditsByUser(Long userId);
  List<AuditLogEntity> auditsByEntityType(String entityType, int limit);
}
