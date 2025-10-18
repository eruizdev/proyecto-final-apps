package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceTypeJpaRepository extends JpaRepository<SpaceTypeEntity, Long> {

  List<SpaceTypeEntity> findByActiveTrue();

  @Query("""
      SELECT DISTINCT st FROM SpaceTypeEntity st
      WHERE st.active = true
        AND (:status IS NULL OR EXISTS (
              SELECT 1 FROM SpaceEntity s
              WHERE s.spaceType = st AND s.spaceStatus = :status
        ))
      """)
  List<SpaceTypeEntity> findByActiveTrueAndSpaceStatus(@Param("status") SpaceEntity.SpaceStatus status);
}
