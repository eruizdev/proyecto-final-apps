package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.AdminWorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminWorkspaceServiceImpl implements AdminWorkspaceService {
  private final SpaceTypeJpaRepository types;
  private final SpaceJpaRepository spaces;

  
  // TYPES
  @Override
  public SpaceTypeEntity createType(SpaceTypeEntity t) {
    var now = LocalDateTime.now();
    t.setActive(t.getActive() != null ? t.getActive() : true);
    t.setCreatedAt(now);
    t.setUpdatedAt(now);
    return types.save(t);
  }

  