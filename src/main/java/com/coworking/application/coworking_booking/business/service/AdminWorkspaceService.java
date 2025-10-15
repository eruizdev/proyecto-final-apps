package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;

import java.util.List;

public interface AdminWorkspaceService {
  // SpaceType
  SpaceTypeEntity createType(SpaceTypeEntity t);
  SpaceTypeEntity updateType(Long id, SpaceTypeEntity t);
  void deleteType(Long id);
  List<SpaceTypeEntity> listTypes();

  // Space
  SpaceEntity createSpace(SpaceEntity s, Long spaceTypeId);
  SpaceEntity updateSpace(Long id, SpaceEntity s, Long spaceTypeId);
  void deleteSpace(Long id);
  List<SpaceEntity> listSpaces();
}
