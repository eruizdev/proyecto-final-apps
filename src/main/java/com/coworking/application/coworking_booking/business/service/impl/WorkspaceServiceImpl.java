package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.repository.WorkspaceRepositoryPort;
import com.coworking.application.coworking_booking.business.service.WorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
  private final WorkspaceRepositoryPort repo;
  public List<SpaceEntity> listActives(){ return repo.findActiveAvailable(); }
  public SpaceEntity get(Long id){ return repo.findSpaceById(id).orElseThrow(() -> new NotFoundException("Espacio")); }
}
