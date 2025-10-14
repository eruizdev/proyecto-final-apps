package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.AuditService;
import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepositoryPort repo;
    private final UserRepositoryPort users;

    // Implementación con 6 parámetros
    @Override
    public void record(Long userId, String entityType, Long entityId, String action, String oldValues, String newValues) {
        var user = users.require(userId);
        repo.save(AuditLogEntity.builder()
                .user(user)
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .oldValues(oldValues)
                .newValues(newValues)
                .createdAt(LocalDateTime.now())
                .build());
    }

    //  implementación de la sobrecarga con 5 parámetros
    @Override
    public void record(Long userId, String entityType, Long entityId, String action, String newValues) {
        // deja a la versión completa dejando oldValues como null
        record(userId, entityType, entityId, action, null, newValues);
    }
}

