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

  
  @Override
  public SpaceTypeEntity updateType(Long id, SpaceTypeEntity t) {
    var cur = types.findById(id).orElseThrow(() -> new NotFoundException("SpaceType"));
    cur.setName(t.getName());
    cur.setDescription(t.getDescription());
    cur.setBasePricePerHour(t.getBasePricePerHour());
    cur.setAmenities(t.getAmenities());
    cur.setActive(t.getActive() != null ? t.getActive() : cur.getActive());
    cur.setUpdatedAt(LocalDateTime.now());
    return types.save(cur);
  }

  @Override
  public void deleteType(Long id) { types.deleteById(id); }

  @Override
  public List<SpaceTypeEntity> listTypes() { return types.findAll(); }


  // SPACES


   @Override
  public SpaceEntity createSpace(SpaceEntity s, Long spaceTypeId) {
    var st = types.findById(spaceTypeId).orElseThrow(() -> new NotFoundException("SpaceType"));
    var now = LocalDateTime.now();
    s.setSpaceType(st);
    s.setSpaceStatus(s.getSpaceStatus() != null ? s.getSpaceStatus() : SpaceEntity.SpaceStatus.AVAILABLE);
    s.setActive(s.getActive());
    s.setCreatedAt(now);
    s.setUpdatedAt(now);
    return spaces.save(s);
  }

   @Override
  public SpaceEntity updateSpace(Long id, SpaceEntity s, Long spaceTypeId) {
    var cur = spaces.findById(id).orElseThrow(() -> new NotFoundException("Space"));
    if (spaceTypeId != null) {
      var st = types.findById(spaceTypeId).orElseThrow(() -> new NotFoundException("SpaceType"));
      cur.setSpaceType(st);
    }
    cur.setName(s.getName());
    cur.setDescription(s.getDescription());
    cur.setCapacity(s.getCapacity());
    cur.setPricePerHour(s.getPricePerHour());
    if (s.getSpaceStatus() != null) cur.setSpaceStatus(s.getSpaceStatus());
    cur.setLocation(s.getLocation());
    cur.setEquipment(s.getEquipment());
    cur.setImages(s.getImages());
    cur.setActive(s.getActive());
    cur.setUpdatedAt(LocalDateTime.now());
    return spaces.save(cur);
  }


  @Override
  public void deleteSpace(Long id) { spaces.deleteById(id); }

  @Override
  public List<SpaceEntity> listSpaces() { return spaces.findAll(); }
}

