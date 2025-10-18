package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceJpaRepository extends JpaRepository<SpaceEntity, Long> {

  List<SpaceEntity> findByActiveTrueAndSpaceStatus(SpaceEntity.SpaceStatus status);

  @Query("""
    SELECT s FROM SpaceEntity s
    WHERE (:typeId IS NULL OR s.spaceType.id = :typeId)
      AND (:capMin IS NULL OR s.capacity >= :capMin)
      AND (:status IS NULL OR s.spaceStatus = :status)
      AND s.active = true
  """)
  List<SpaceEntity> search(@Param("typeId") Long typeId,
                           @Param("capMin") Integer capMin,
                           @Param("status") SpaceEntity.SpaceStatus status);
}
