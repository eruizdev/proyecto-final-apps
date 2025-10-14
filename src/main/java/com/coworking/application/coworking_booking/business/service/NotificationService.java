package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import java.util.List;

public interface NotificationService {
    void notifyBookingCreated(Long userId, Long bookingId);
    void notifyBookingCancelled(Long userId, Long bookingId);
    List<NotificationEntity> unread(Long userId);
}


