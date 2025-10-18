package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.repository.*;
import com.coworking.application.coworking_booking.business.service.AuditService;
import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.business.service.SubscriptionService;
import com.coworking.application.coworking_booking.business.service.impl.BookingServiceImpl;
import com.coworking.application.coworking_booking.persistence.entity.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingRepositoryPort bookingRepo;
    private WorkspaceRepositoryPort workspaceRepo;
    private UserRepositoryPort userRepo;
    private PaymentRepositoryPort paymentRepo;
    private AuditService auditService;
    private NotificationService notificationService;
    private SubscriptionService subscriptionService;

    private BookingServiceImpl service;

    @BeforeEach
    void setup() {
        bookingRepo = mock(BookingRepositoryPort.class);
        workspaceRepo = mock(WorkspaceRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);
        paymentRepo = mock(PaymentRepositoryPort.class);
        auditService = mock(AuditService.class);
        notificationService = mock(NotificationService.class);
        subscriptionService = mock(SubscriptionService.class);

        service = new BookingServiceImpl(
                bookingRepo,
                workspaceRepo,
                userRepo,
                paymentRepo,
                auditService,
                notificationService,
                subscriptionService
        );
    }

    @Test
    void createBookingSuccess() {
        var user = UserEntity.builder().id(1L).build();
        var space = SpaceEntity.builder()
                .id(2L)
                .pricePerHour(BigDecimal.valueOf(10000))
                .active(true)
                .build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(workspaceRepo.findSpaceById(2L)).thenReturn(Optional.of(space));
        when(bookingRepo.existsOverlap(any(), any(), any(), any())).thenReturn(false);
        when(bookingRepo.countFutureByUser(1L)).thenReturn(0);
        when(bookingRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(paymentRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(subscriptionService.hasActive(1L)).thenReturn(false);

        var now = LocalDateTime.now();
        var booking = service.create(1L, 2L, now.plusHours(1), now.plusHours(2), 3);

        assertEquals(BigDecimal.valueOf(10000), booking.getTotalAmount());
        verify(paymentRepo).save(any());
        verify(notificationService).notifyBookingCreated(eq(1L), any());
    }

    @Test
    void cancelBookingMarksAsCancelled() {
        var booking = BookingEntity.builder()
                .id(5L)
                .user(UserEntity.builder().id(1L).build())
                .build();

        when(bookingRepo.findById(5L)).thenReturn(Optional.of(booking));
        when(paymentRepo.findByBookingId(5L))
                .thenReturn(Optional.of(PaymentEntity.builder().build()));

        service.cancel(5L);

        verify(bookingRepo).save(any());
        verify(auditService).record(eq(1L), eq("BOOKING"), eq(5L),
                eq("CANCEL"), any(), any());
    }

    @Test
    void listByUserReturnsLimitedResults() {
        when(bookingRepo.findByUser(1L)).thenReturn(List.of(
                BookingEntity.builder().id(1L).build(),
                BookingEntity.builder().id(2L).build()
        ));

        var list = service.listByUser(1L, 1);
        assertEquals(1, list.size());
    }

    @Test
    void getThrowsIfNotFound() {
        when(bookingRepo.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(10L));
    }
}
