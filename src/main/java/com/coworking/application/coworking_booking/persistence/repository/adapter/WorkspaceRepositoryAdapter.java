package com.coworking.application.coworking_booking.persistence.repository.adapter;

import com.coworking.application.coworking_booking.business.repository.*;
import com.coworking.application.coworking_booking.persistence.entity.*;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component @RequiredArgsConstructor
public class WorkspaceRepositoryAdapter implements WorkspaceRepositoryPort {
	
	private final SpaceJpaRepository jpa;
	  public Optional<SpaceEntity> findSpaceById(Long id){ return jpa.findById(id); }
	  public List<SpaceEntity> findActiveAvailable(){
	    return jpa.findByActiveTrueAndSpaceStatus(SpaceEntity.SpaceStatus.AVAILABLE);
	  }
}