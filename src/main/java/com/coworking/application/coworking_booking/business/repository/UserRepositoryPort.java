package com.coworking.application.coworking_booking.business.repository;

import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import java.util.*;

public interface UserRepositoryPort {
    Optional<UserEntity> findById(Long id);
    UserEntity save(UserEntity u);
    List<UserEntity> findAll(); // 

    default UserEntity require(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
