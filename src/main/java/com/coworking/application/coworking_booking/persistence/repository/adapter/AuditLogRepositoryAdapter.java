package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {
	
	private final AuditLogJpaRepository jpa;

    @Override
    public AuditLogEntity save(AuditLogEntity log) {
        return jpa.save(log);
    }
		
}