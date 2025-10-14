package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;

public interface AuditLogRepositoryPort {
    AuditLogEntity save(AuditLogEntity log);
}

