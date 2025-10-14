package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.NotificationRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepositoryPort repo;
    private final UserRepositoryPort users;

    package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.NotificationRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepositoryPort repo;
    private final UserRepositoryPort users;

    @Override
    public void notifyBookingCreated(Long userId, Long bookingId) {
        var user = users.require(userId);
        repo.save(NotificationEntity.builder()
                .user(user)
                .title("Reserva confirmada")
                .message("Tu reserva #" + bookingId + " ha sido confirmada.")
                .notificationType(NotificationEntity.NotificationType.BOOKING_CONFIRMATION)
                .createdAt(LocalDateTime.now())
                .build());
    }

    
    @Override
    public void notifyBookingCancelled(Long userId, Long bookingId) {
        var user = users.require(userId);
        repo.save(NotificationEntity.builder()
                .user(user)
                .title("Reserva cancelada")
                .message("Tu reserva #" + bookingId + " ha sido cancelada.")
                .notificationType(NotificationEntity.NotificationType.BOOKING_CANCELLED) // ← aquí el cambio
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    public List<NotificationEntity> unread(Long userId) {
        return repo.unreadByUser(userId);
    }
}

