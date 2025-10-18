package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.repository.WorkspaceRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.WorkspaceServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceTest {
    private WorkspaceRepositoryPort repo;
    private WorkspaceServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(WorkspaceRepositoryPort.class);
        service = new WorkspaceServiceImpl(repo);
    }

    @Test
    void listActivesReturnsSpaces() {
        when(repo.findActiveAvailable()).thenReturn(List.of(SpaceEntity.builder().id(1L).build()));
        var list = service.listActives();
        assertEquals(1, list.size());
    }

    @Test
    void getReturnsEntity() {
        when(repo.findSpaceById(1L)).thenReturn(Optional.of(SpaceEntity.builder().id(1L).build()));
        var result = service.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getThrowsIfNotFound() {
        when(repo.findSpaceById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }
}
