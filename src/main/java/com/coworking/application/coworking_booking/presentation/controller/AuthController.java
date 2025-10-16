package com.coworking.application.coworking_booking.presentation.controller;

// importes
import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthRequestDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthResponseDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.LoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controlador para la autenticacion y autorizacion
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Dependencias
    private final AuthService authService;
    private final UserJpaRepository users;

    public AuthController(AuthService authService, UserJpaRepository users) {
        this.authService = authService;
        this.users = users;
    }

    // Registro de usuarios (sin cambios)
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO req) {
        var u = authService.register(req.email(), req.password(), req.firstName(), req.lastName());
        return ResponseEntity.ok(new AuthResponseDTO(u.getId(), u.getEmail(), u.getUserRole().name()));
    }

    // Login que devuelve el token directamente (simple y limpio)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(java.util.Map.of("token", token));
    }
}