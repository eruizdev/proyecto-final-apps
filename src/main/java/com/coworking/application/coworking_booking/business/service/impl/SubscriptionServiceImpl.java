package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.SubscriptionRepositoryPort;
import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.SubscriptionService;
import com.coworking.application.coworking_booking.persistence.entity.UserSubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
  private final SubscriptionRepositoryPort repo;
  private final UserRepositoryPort users;

  @Transactional
  @Override
  public UserSubscriptionEntity activate(Long userId, Long planId){
    var user = users.require(userId);
    var plan = repo.requirePlan(planId);
    repo.findActiveByUser(userId).ifPresent(s -> {
      s.setActive(false); s.setEndAt(LocalDateTime.now()); repo.save(s);
    });
    var now = LocalDateTime.now();
    return repo.save(UserSubscriptionEntity.builder()
        .user(user).plan(plan).active(true).startAt(now).createdAt(now).updatedAt(now).build());
  }

  @Override
  public UserSubscriptionEntity me(Long userId){
    return repo.findActiveByUser(userId).orElse(null);
  }

  @Override
  public boolean hasActive(Long userId){ return repo.findActiveByUser(userId).isPresent(); }
}
