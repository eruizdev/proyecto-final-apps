package com.coworking.application.coworking_booking.persistence.repository.spring;

import com.coworking.application.coworking_booking.persistence.entity.MembershipPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPlanJpaRepository extends JpaRepository<MembershipPlanEntity, Long> {}
