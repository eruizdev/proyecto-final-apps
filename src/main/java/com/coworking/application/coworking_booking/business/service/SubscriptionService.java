package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.UserSubscriptionEntity;

public interface SubscriptionService {
  UserSubscriptionEntity activate(Long userId, Long planId);
  UserSubscriptionEntity me(Long userId);
  boolean hasActive(Long userId);
}
