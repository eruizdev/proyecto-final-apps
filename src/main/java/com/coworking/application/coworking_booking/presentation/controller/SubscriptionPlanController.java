package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.repository.spring.SubscriptionPlanJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.subscription.SubscriptionPlanResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

  private final SubscriptionPlanJpaRepository plans;

  public SubscriptionPlanController(SubscriptionPlanJpaRepository plans) {
    this.plans = plans;
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping
  @Operation(summary = "Listar planes de suscripción", description = "Devuelve todos los planes disponibles")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> list() {
    try {
      var body = plans.findAll().stream()
          .map(p -> new SubscriptionPlanResponseDTO(
              p.getId(), p.getType(), p.getPrice(), p.isActive()
          ))
          .toList();
      return ResponseEntity.ok(body);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }
}
