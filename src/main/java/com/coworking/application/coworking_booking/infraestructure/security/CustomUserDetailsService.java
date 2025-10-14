package com.coworking.application.coworking_booking.infraestructure.security;

import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
}