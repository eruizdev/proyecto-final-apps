package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository jpa;

  @Override
  public Optional<UserEntity> findById(Long id) {
    return jpa.findById(id);
  }

  @Override
  public UserEntity save(UserEntity u) {
    return jpa.save(u);
  }

  @Override
  public List<UserEntity> findAll() {
    return jpa.findAll();
  }
}
