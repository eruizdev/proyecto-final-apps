package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.*;

import java.time.LocalDateTime;

public interface WorkspaceService {
  java.util.List<SpaceEntity> listActives();
  SpaceEntity get(Long id);
}

