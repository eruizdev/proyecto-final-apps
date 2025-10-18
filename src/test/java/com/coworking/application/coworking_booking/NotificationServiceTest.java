package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.repository.NotificationRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.NotificationServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
    private NotificationRepositoryPort repo;
    private UserRepositoryPort users;
    private NotificationServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(NotificationRepositoryPort.class);
        users = mock(UserRepositoryPort.class);
        service = new NotificationServiceImpl(repo, users);
    }

    @Test
    void notifyBookingCreatedSaves() {
        when(users.require(1L)).thenReturn(UserEntity.builder().id(1L).build());
        service.notifyBookingCreated(1L, 11L);
        verify(repo).save(any(NotificationEntity.class));
    }

    @Test
    void notifyBookingCancelledSaves() {
        when(users.require(1L)).thenReturn(UserEntity.builder().id(1L).build());
        service.notifyBookingCancelled(1L, 11L);
        verify(repo).save(any(NotificationEntity.class));
    }

    @Test
    void unreadReturnsList() {
        when(repo.unreadByUser(1L)).thenReturn(List.of(NotificationEntity.builder().title("n1").build()));
        var result = service.unread(1L);
        assertEquals("n1", result.get(0).getTitle());
    }
}
