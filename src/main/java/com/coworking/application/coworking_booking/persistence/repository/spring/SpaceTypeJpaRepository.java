package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceTypeJpaRepository extends JpaRepository<SpaceTypeEntity, Long> {
}
