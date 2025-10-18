package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.repository.AuditLogRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.AdminServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.AuditLogEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private AuditLogRepositoryPort repo;
    private AdminServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(AuditLogRepositoryPort.class);
        service = new AdminServiceImpl(repo);
    }

    @Test
    void auditsAllReturnsAll() {
        when(repo.listAll()).thenReturn(List.of(AuditLogEntity.builder().id(1L).createdAt(LocalDateTime.now()).build()));
        assertEquals(1, service.auditsAll().size());
    }

    @Test
    void auditsByUserFilters() {
        when(repo.listByUser(10L)).thenReturn(List.of(AuditLogEntity.builder().id(99L).build()));
        assertEquals(99L, service.auditsByUser(10L).get(0).getId());
    }

    @Test
    void auditsByEntityTypeWorks() {
        when(repo.listByEntityType("USER", 5)).thenReturn(List.of(AuditLogEntity.builder().entityType("USER").build()));
        assertEquals("USER", service.auditsByEntityType("USER", 5).get(0).getEntityType());
    }
}
