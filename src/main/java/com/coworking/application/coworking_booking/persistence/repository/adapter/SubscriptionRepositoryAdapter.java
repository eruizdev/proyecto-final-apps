package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.SubscriptionRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component @RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {
  private final UserSubscriptionJpaRepository subs;
  private final MembershipPlanJpaRepository plans;

  @Override public Optional<UserSubscriptionEntity> findActiveByUser(Long userId){
    return subs.findFirstByUserIdAndActiveTrue(userId);
  }
  @Override public UserSubscriptionEntity save(UserSubscriptionEntity s){ return subs.save(s); }
  @Override public MembershipPlanEntity requirePlan(Long planId){
    return plans.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan no existe"));
  }
}