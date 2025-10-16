package com.coworking.application.coworking_booking;

// importes necesarios
import com.coworking.application.coworking_booking.infraestructure.security.JwtProvider;
import com.coworking.application.coworking_booking.persistence.entity.UserEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Anotaciones para configurar el entorno de pruebas
@SpringBootTest
@AutoConfigureMockMvc
class AdminSecurityCrudTest {

    // Configuración inicial antes de cada prueba
  @Autowired MockMvc mvc;
  @Autowired JwtProvider jwt;
  @Autowired UserJpaRepository users;

  // Configuración inicial antes de cada prueba
  String userToken;
  String adminToken;
  
}
