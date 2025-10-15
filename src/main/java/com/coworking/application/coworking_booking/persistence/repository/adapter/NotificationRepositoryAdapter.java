package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.NotificationRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {
	
	private final NotificationJpaRepository jpa;

    @Override
    public NotificationEntity save(NotificationEntity n) {
        return jpa.save(n);
    }

    @Override
    public List<NotificationEntity> unreadByUser(Long userId) {
        return jpa.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }
}