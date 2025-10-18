package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.*;
import java.util.Optional;

public interface SubscriptionRepositoryPort {
  Optional<UserSubscriptionEntity> findActiveByUser(Long userId);
  UserSubscriptionEntity save(UserSubscriptionEntity s);
  MembershipPlanEntity requirePlan(Long planId);
}
