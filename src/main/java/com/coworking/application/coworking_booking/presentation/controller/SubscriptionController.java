package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.SubscriptionService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/subscriptions")
@PreAuthorize("hasRole('USER')")
public class SubscriptionController {
  private final SubscriptionService service;
  public SubscriptionController(SubscriptionService s){ this.service = s; }

  @PostMapping
  @Operation(summary = "Activar suscripción", description = "Activa un plan para un usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> activate(@RequestParam Long userId, @RequestParam Long planId){
    try {
      var sub = service.activate(userId, planId);
      return ResponseEntity.ok(java.util.Map.of("id", sub.getId(), "userId", userId, "plan", sub.getPlan().getName(), "active", sub.isActive()));
    } catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @GetMapping("/me")
  @Operation(summary = "Mi suscripción", description = "Devuelve el estado de suscripción de un usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> me(@RequestParam Long userId){
    try {
      var s = service.me(userId);
      return ResponseEntity.ok( s==null ? java.util.Map.of("active", false) :
          java.util.Map.of("active", true, "plan", s.getPlan().getName(), "startAt", s.getStartAt()) );
    } catch (BusinessException | IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
