package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.NotificationJpaRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService service;
  private final NotificationJpaRepository repo;

  public NotificationController(NotificationService service, NotificationJpaRepository repo) {
    this.service = service;
    this.repo = repo;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/unread/{userId}")
  @Operation(summary = "No leídas del usuario", description = "Lista notificaciones no leídas por userId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> unread(@PathVariable Long userId) {
    try {
      var list = service.unread(userId).stream().map(n -> java.util.Map.of(
          "id", n.getId(),
          "title", n.getTitle(),
          "message", n.getMessage(),
          "type", n.getNotificationType().name(),
          "createdAt", n.getCreatedAt()
      )).toList();
      return ResponseEntity.ok(list);
    } catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  @Operation(summary = "Todas las notificaciones", description = "Solo ADMIN")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> allNotifications() {
    try {
      List<NotificationEntity> list = repo.findAll();
      return ResponseEntity.ok(list);
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping
  @Operation(summary = "Mis notificaciones", description = "Devuelve notificaciones del usuario autenticado")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> myNotifications(Authentication auth) {
    try {
      return ResponseEntity.ok(repo.findAll());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
