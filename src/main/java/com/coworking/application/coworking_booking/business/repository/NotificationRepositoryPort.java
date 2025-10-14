package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import java.util.List;

public interface NotificationRepositoryPort {
    NotificationEntity save(NotificationEntity n);
    List<NotificationEntity> unreadByUser(Long userId);
}
