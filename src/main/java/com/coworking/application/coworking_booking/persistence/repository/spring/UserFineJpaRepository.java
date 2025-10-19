package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.UserFineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFineJpaRepository extends JpaRepository<UserFineEntity, Long> {
  List<UserFineEntity> findAllByOrderByCreatedAtDesc();
  List<UserFineEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
