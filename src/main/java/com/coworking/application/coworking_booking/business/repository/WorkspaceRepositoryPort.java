package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import java.util.List;
import java.util.Optional;

public interface WorkspaceRepositoryPort {
    Optional<SpaceEntity> findSpaceById(Long id);
    List<SpaceEntity> findActiveAvailable();
}
