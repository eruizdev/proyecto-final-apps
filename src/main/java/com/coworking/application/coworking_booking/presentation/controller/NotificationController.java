package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.repository.spring.NotificationJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationJpaRepository repo;

  public NotificationController(NotificationJpaRepository repo) {
    this.repo = repo;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @Operation(
      summary = "Todas las notificaciones",
      description = "Solo ADMIN. Muestra todas las notificaciones del sistema."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "401", description = "No autenticado"),
      @ApiResponse(responseCode = "403", description = "Prohibido"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> all() {
    try {
      return ResponseEntity.ok(repo.findAll());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }
}
