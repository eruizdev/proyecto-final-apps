package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.policy.BookingPolicies;
import com.coworking.application.coworking_booking.business.repository.*;
import com.coworking.application.coworking_booking.business.service.*;
import com.coworking.application.coworking_booking.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepositoryPort bookingRepo;
    private final WorkspaceRepositoryPort workspaceRepo;
    private final UserRepositoryPort userRepo;
    private final PaymentRepositoryPort paymentRepo;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService; // 🔹 NUEVO

    private final BookingPolicies policies = new BookingPolicies();

    @Transactional
    public BookingEntity create(Long userId, Long spaceId, LocalDateTime start, LocalDateTime end, int attendees) {
        var user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Usuario"));
        var space = workspaceRepo.findSpaceById(spaceId).orElseThrow(() -> new NotFoundException("Espacio"));

        boolean overlap = bookingRepo.existsOverlap(spaceId, start, end,
                Set.of(BookingEntity.BookingStatus.PENDING, BookingEntity.BookingStatus.CONFIRMED));
        int futureCount = bookingRepo.countFutureByUser(user.getId());
        policies.validateCreation(space, start, end, attendees, overlap, futureCount);

        var total = space.getPricePerHour().multiply(BigDecimal.valueOf(
                Math.max(1, java.time.Duration.between(start, end).toHours())));

        var now = LocalDateTime.now();
        var booking = BookingEntity.builder()
                .user(user)
                .space(space)
                .startTime(start)
                .endTime(end)
                .bookingStatus(BookingEntity.BookingStatus.CONFIRMED)
                .attendees(attendees)
                .totalAmount(total)
                .createdAt(now)
                .updatedAt(now)
                .build();

        booking = bookingRepo.save(booking);

        // 🔹 Detectar si tiene suscripción activa
        boolean viaSub = subscriptionService != null && subscriptionService.hasActive(userId);

        paymentRepo.save(PaymentEntity.builder()
                .booking(booking)
                .amount(total)
                .paymentStatus(PaymentEntity.PaymentStatus.PAID)
                .paymentMethod(viaSub ? "SUBSCRIPTION" : "ONLINE")
                .paymentDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build());

        // 🔔 Notificación + Auditoría completa (6 parámetros)
        notificationService.notifyBookingCreated(userId, booking.getId());
        auditService.record(userId, "BOOKING", booking.getId(), "CREATE", null, "{\"status\":\"CONFIRMED\"}");

        return booking;
    }

    @Transactional(readOnly = true)
    public BookingEntity get(Long id) {
        return bookingRepo.findById(id).orElseThrow(() -> new NotFoundException("Reserva"));
    }

    @Transactional
    public void cancel(Long bookingId) {
        var booking = bookingRepo.findById(bookingId).orElseThrow(() -> new NotFoundException("Reserva"));
        booking.setBookingStatus(BookingEntity.BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepo.save(booking);

        paymentRepo.findByBookingId(bookingId).ifPresent(p -> {
            p.setPaymentStatus(PaymentEntity.PaymentStatus.REFUNDED);
            p.setUpdatedAt(LocalDateTime.now());
            paymentRepo.save(p);
        });

        // 🔔 Notificación + Auditoría completa (6 parámetros)
        notificationService.notifyBookingCancelled(booking.getUser().getId(), booking.getId());
        auditService.record(
                booking.getUser().getId(),
                "BOOKING",
                booking.getId(),
                "CANCEL",
                "{\"status\":\"CONFIRMED\"}",
                "{\"status\":\"CANCELLED\"}"
        );
    }

    // 🔹 NUEVO método fusionado del segundo fragmento
    @Transactional(readOnly = true)
    public List<BookingEntity> listByUser(Long userId, int limit) {
        return bookingRepo.findByUser(userId).stream().limit(limit).toList();
    }
}
