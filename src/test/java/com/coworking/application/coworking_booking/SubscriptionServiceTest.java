package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.repository.SubscriptionRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.SubscriptionServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.MembershipPlanEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserSubscriptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {
    private SubscriptionRepositoryPort repo;
    private UserRepositoryPort users;
    private SubscriptionServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(SubscriptionRepositoryPort.class);
        users = mock(UserRepositoryPort.class);
        service = new SubscriptionServiceImpl(repo, users);
    }

    @Test
    void activateCreatesNewSub() {
        var u = UserEntity.builder().id(1L).build();
        var p = MembershipPlanEntity.builder().id(2L).build();
        when(users.require(1L)).thenReturn(u);
        when(repo.requirePlan(2L)).thenReturn(p);
        when(repo.findActiveByUser(1L)).thenReturn(Optional.empty());
        when(repo.save(any())).thenReturn(UserSubscriptionEntity.builder().id(1L).build());
        var result = service.activate(1L, 2L);
        assertEquals(1L, result.getId());
    }

    @Test
    void hasActiveTrue() {
        when(repo.findActiveByUser(1L)).thenReturn(Optional.of(new UserSubscriptionEntity()));
        assertTrue(service.hasActive(1L));
    }

    @Test
    void hasActiveFalse() {
        when(repo.findActiveByUser(1L)).thenReturn(Optional.empty());
        assertFalse(service.hasActive(1L));
    }
}
