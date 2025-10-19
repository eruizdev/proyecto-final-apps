package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.exception.ValidationException;
import com.coworking.application.coworking_booking.business.service.AuthService;
import com.coworking.application.coworking_booking.persistence.repository.spring.UserJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthRequestDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.AuthResponseDTO;
import com.coworking.application.coworking_booking.presentation.dto.auth.LoginRequestDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserJpaRepository users;

  public AuthController(AuthService authService, UserJpaRepository users) {
    this.authService = authService;
    this.users = users;
  }

  @PostMapping("/register")
  @Operation(summary = "Registrar usuario", description = "Crea un usuario y devuelve datos básicos")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Registrado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "409", description = "Email ya registrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> register(@RequestBody AuthRequestDTO req) {
    try {
      var u = authService.register(req.email(), req.password(), req.firstName(), req.lastName());
      return ResponseEntity.ok(new AuthResponseDTO(u.getId(), u.getEmail(), u.getUserRole().name()));
    } catch (ValidationException e) {
     
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    } catch (BusinessException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PostMapping("/login")
  @Operation(summary = "Login", description = "Autentica al usuario y devuelve un token JWT")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado / credenciales"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
    try {
      String token = authService.login(req.email(), req.password());
      return ResponseEntity.ok(java.util.Map.of("token", token));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
