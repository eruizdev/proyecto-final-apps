package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.entity.SubscriptionPlanEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.SubscriptionPlanJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.subscription.SubscriptionPlanCreateDTO;
import com.coworking.application.coworking_booking.presentation.dto.subscription.SubscriptionPlanResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subscription-plans")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubscriptionPlanController {

  private final SubscriptionPlanJpaRepository plans;

  public AdminSubscriptionPlanController(SubscriptionPlanJpaRepository plans) {
    this.plans = plans;
  }

  @PostMapping
  @Operation(summary = "Crear plan de suscripción", description = "ADMIN crea un plan (tipo y precio)")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Creado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> create(@RequestBody SubscriptionPlanCreateDTO dto) {
    try {
      if (dto.type() == null || dto.type().isBlank()) {
        return ResponseEntity.badRequest().body("type es requerido");
      }
      if (dto.price() == null || dto.price().signum() < 0) {
        return ResponseEntity.badRequest().body("price inválido");
      }
      if (plans.findByTypeIgnoreCase(dto.type()).isPresent()) {
        return ResponseEntity.badRequest().body("Ya existe un plan con ese tipo");
      }
      var saved = plans.save(SubscriptionPlanEntity.builder()
          .type(dto.type().trim())
          .price(dto.price())
          .active(true)
          .build());
      return ResponseEntity.ok(new SubscriptionPlanResponseDTO(
          saved.getId(), saved.getType(), saved.getPrice(), saved.isActive()
      ));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }
}
