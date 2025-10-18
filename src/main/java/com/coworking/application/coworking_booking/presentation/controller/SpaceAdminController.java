package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController @RequestMapping("/api/admin/spaces")
@PreAuthorize("hasRole('ADMIN')")
public class SpaceAdminController {
  private final SpaceJpaRepository spaces;
  private final SpaceTypeJpaRepository types;

  public SpaceAdminController(SpaceJpaRepository s, SpaceTypeJpaRepository t){ this.spaces = s; this.types = t; }

  @PostMapping
  public Map<String,Object> create(@RequestBody Map<String,Object> body){
    var now = LocalDateTime.now();
    var space = SpaceEntity.builder()
        .spaceType(types.findById(Long.valueOf(body.get("spaceTypeId").toString())).orElseThrow())
        .name(body.get("name").toString())
        .capacity(Integer.valueOf(body.get("capacity").toString()))
        .pricePerHour(new java.math.BigDecimal(body.get("pricePerHour").toString()))
        .spaceStatus(SpaceEntity.SpaceStatus.valueOf(body.get("status").toString()))
        .active(true).createdAt(now).updatedAt(now).build();
    space = spaces.save(space);
    return Map.of("id", space.getId(), "name", space.getName());
  }

  @PutMapping("/{id}")
  public Map<String,Object> update(@PathVariable Long id, @RequestBody Map<String,Object> body){
    var s = spaces.findById(id).orElseThrow();
    if (body.containsKey("name")) s.setName(body.get("name").toString());
    if (body.containsKey("capacity")) s.setCapacity(Integer.valueOf(body.get("capacity").toString()));
    if (body.containsKey("pricePerHour")) s.setPricePerHour(new java.math.BigDecimal(body.get("pricePerHour").toString()));
    if (body.containsKey("status")) s.setSpaceStatus(SpaceEntity.SpaceStatus.valueOf(body.get("status").toString()));
    s.setUpdatedAt(LocalDateTime.now());
    spaces.save(s);
    return Map.of("id", s.getId(), "updated", true);
  }

  @DeleteMapping("/{id}")
  public Map<String,Object> delete(@PathVariable Long id){
    spaces.deleteById(id);
    return Map.of("id", id, "deleted", true);
  }
}
