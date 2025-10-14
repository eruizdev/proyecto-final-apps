package com.coworking.application.coworking_booking.infraestructure.security;

import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserJpaRepository users;

	  @Override
	  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    var u = users.findAll().stream()
	        .filter(x -> x.getEmail().equalsIgnoreCase(email))
	        .findFirst()
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    return new org.springframework.security.core.userdetails.User(
	        u.getEmail(), u.getPasswordHash(),
	        List.of(() -> "ROLE_" + u.getUserRole().name())
	    );
	  }
}