package main.java.com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthRequestDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthResponseDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.LoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
}