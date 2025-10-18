package com.coworking.application.coworking_booking;

import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.impl.AuthServiceImpl;
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private UserRepositoryPort users;
    private JwtProvider jwt;
    private UserJpaRepository jpa;
    private AuthServiceImpl service;

    @BeforeEach
    void setup() {
        users = mock(UserRepositoryPort.class);
        jwt = mock(JwtProvider.class);
        jpa = mock(UserJpaRepository.class);
        service = new AuthServiceImpl(users, jwt, jpa);
    }

    @Test
    void registerCreatesUser() {
        when(users.save(any())).thenAnswer(a -> a.getArgument(0));
        var user = service.register("test@a.com", "123", "A", "B");
        assertEquals("test@a.com", user.getEmail());
    }

    @Test
    void loginGeneratesToken() {
        var u = UserEntity.builder().id(1L).email("x@x.com").passwordHash("{noop}a").userRole(UserEntity.Role.USER).build();
        when(jpa.findByEmail("x@x.com")).thenReturn(Optional.of(u));
        when(jwt.generateToken(1L, "x@x.com", "USER")).thenReturn("jwt123");
        assertEquals("jwt123", service.login("x@x.com", "a"));
    }

    @Test
    void loginThrowsInvalidPassword() {
        var u = UserEntity.builder().id(1L).email("x@x.com").passwordHash("{noop}good").build();
        when(jpa.findByEmail("x@x.com")).thenReturn(Optional.of(u));
        assertThrows(RuntimeException.class, () -> service.login("x@x.com", "wrong"));
    }
}
