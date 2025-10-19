package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final com.coworking.application.coworking_booking.business.service.AdminService service;
  private final BookingJpaRepository bookingRepo;

  public AdminController(
      com.coworking.application.coworking_booking.business.service.AdminService s,
      BookingJpaRepository b
  ) {
    this.service = s;
    this.bookingRepo = b;
  }

  // ------------------------- AUDITS -------------------------
  @DeleteMapping // (solo para que se parezca al ejemplo) — no hay delete aquí
  private ResponseEntity<Void> noop() { return ResponseEntity.ok().build(); }

  @GetMapping("/audits")
  @Operation(summary = "Listar auditorías", description = "Devuelve todas las auditorías")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> auditsAll() {
    try {
      var body = service.auditsAll().stream().map(a -> {
        Map<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("userId", a.getUser().getId());
        m.put("entityType", a.getEntityType());
        m.put("entityId", a.getEntityId());
        m.put("action", a.getAction());
        m.put("createdAt", a.getCreatedAt());
        return m;
      }).toList();
      return ResponseEntity.ok(body); // 200
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage()); // 400
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor"); // 500
    }
  }

  @GetMapping("/audits/user/{userId}")
  @Operation(summary = "Auditorías por usuario", description = "Devuelve auditorías filtradas por userId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> auditsByUser(@PathVariable Long userId) {
    try {
      var body = service.auditsByUser(userId).stream().map(a -> {
        Map<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("userId", a.getUser().getId());
        m.put("entityType", a.getEntityType());
        m.put("entityId", a.getEntityId());
        m.put("action", a.getAction());
        m.put("createdAt", a.getCreatedAt());
        return m;
      }).toList();
      return ResponseEntity.ok(body);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @GetMapping("/audits/entity/{entityType}")
  @Operation(summary = "Auditorías por entidad", description = "Devuelve auditorías filtradas por tipo de entidad")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> auditsByEntityType(@PathVariable String entityType,
                                              @RequestParam(defaultValue = "50") int limit) {
    try {
      var body = service.auditsByEntityType(entityType.toUpperCase(), limit).stream().map(a -> {
        Map<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("userId", a.getUser().getId());
        m.put("entityType", a.getEntityType());
        m.put("entityId", a.getEntityId());
        m.put("action", a.getAction());
        m.put("createdAt", a.getCreatedAt());
        return m;
      }).toList();
      return ResponseEntity.ok(body);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  // ----------------------- REPORTS -----------------------
  @GetMapping("/reports/bookings")
  @Operation(summary = "Reporte de reservas", description = "Cuenta reservas en un rango de fechas")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> bookings(@RequestParam String from,
                                    @RequestParam String to,
                                    @RequestParam(required = false) Long spaceId) {
    try {
      var f = LocalDateTime.parse(from);
      var t = LocalDateTime.parse(to);
      return ResponseEntity.ok(Map.of("count", bookingRepo.countBookings(f, t, spaceId)));
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Formato de fecha inválido"); // 400
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage()); // 400
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor"); // 500
    }
  }

  @GetMapping("/reports/revenue")
  @Operation(summary = "Reporte de ingresos", description = "Suma ingresos en un rango de fechas")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> revenue(@RequestParam String from,
                                   @RequestParam String to) {
    try {
      var f = LocalDateTime.parse(from);
      var t = LocalDateTime.parse(to);
      return ResponseEntity.ok(Map.of("revenue", bookingRepo.sumRevenue(f, t)));
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Formato de fecha inválido");
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
