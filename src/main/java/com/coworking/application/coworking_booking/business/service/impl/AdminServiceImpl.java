package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
// usamos FQN para AdminService (evita el problema de import en el IDE)
import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl
    implements com.coworking.application.coworking_booking.business.service.AdminService {

  private final AuditLogRepositoryPort repo;

  @Override
  public List<AuditLogEntity> auditsAll() { return repo.listAll(); }

  @Override
  public List<AuditLogEntity> auditsByUser(Long userId) { return repo.listByUser(userId); }

  @Override
  public List<AuditLogEntity> auditsByEntityType(String entityType, int limit) {
    return repo.listByEntityType(entityType, limit);
  }
}
