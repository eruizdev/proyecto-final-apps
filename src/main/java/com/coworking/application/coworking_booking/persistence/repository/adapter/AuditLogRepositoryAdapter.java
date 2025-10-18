package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {

  private final AuditLogJpaRepository jpa;

  @Override
  public AuditLogEntity save(AuditLogEntity log) { return jpa.save(log); }

  @Override
  public List<AuditLogEntity> listAll() { return jpa.findAllByOrderByCreatedAtDesc(); }

  @Override
  public List<AuditLogEntity> listByUser(Long userId) { return jpa.findByUserIdOrderByCreatedAtDesc(userId); }

  @Override
  public List<AuditLogEntity> listByEntityType(String entityType, int limit) {
    return jpa.findByEntityTypeOrderByCreatedAtDesc(entityType, PageRequest.of(0, Math.max(1, limit)));
  }
}
