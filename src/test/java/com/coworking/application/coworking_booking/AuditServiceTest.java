package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.AuditServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuditServiceTest {
    private AuditLogRepositoryPort repo;
    private UserRepositoryPort users;
    private AuditServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(AuditLogRepositoryPort.class);
        users = mock(UserRepositoryPort.class);
        service = new AuditServiceImpl(repo, users);
    }

    @Test
    void recordCreatesAudit() {
        when(users.require(1L)).thenReturn(UserEntity.builder().id(1L).build());
        assertDoesNotThrow(() -> service.record(1L, "SPACE", 2L, "CREATE", null, "{\"x\":1}"));
        verify(repo).save(any());
    }

    @Test
    void recordUsesOverload() {
        when(users.require(2L)).thenReturn(UserEntity.builder().id(2L).build());
        service.record(2L, "BOOKING", 1L, "UPDATE", "{\"status\":\"ok\"}");
        verify(repo, atLeastOnce()).save(any());
    }
}
