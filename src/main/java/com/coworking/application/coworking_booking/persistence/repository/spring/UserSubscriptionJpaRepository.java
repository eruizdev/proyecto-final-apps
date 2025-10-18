package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSubscriptionJpaRepository extends JpaRepository<UserSubscriptionEntity, Long> {
  Optional<UserSubscriptionEntity> findFirstByUserIdAndActiveTrue(Long userId);
  boolean existsByEmail(String email);
  Optional<UserEntity> findByEmail(String email);
}
