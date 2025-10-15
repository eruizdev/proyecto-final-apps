package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpaceJpaRepository extends JpaRepository<SpaceEntity, Long> {
    List<SpaceEntity> findByActiveTrueAndSpaceStatus(SpaceEntity.SpaceStatus status);
}
