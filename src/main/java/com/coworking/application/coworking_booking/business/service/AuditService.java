package com.coworking.application.coworking_booking.business.service;

public interface AuditService {
    void record(Long userId, String entityType, Long entityId, String action, String oldValues, String newValues);

    void record(Long userId, String entityType, Long entityId, String action, String newValues);
}

