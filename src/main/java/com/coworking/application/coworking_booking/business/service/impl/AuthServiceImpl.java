package com.coworking.application.coworking_booking.business.service.impl;

import com.coworking.application.coworking_booking.business.repository.UserRepositoryPort;
import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort users;
    private final JwtProvider jwt; // inyectamos JwtProvider para generar el token