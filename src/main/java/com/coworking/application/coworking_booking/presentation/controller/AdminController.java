package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import com.coworking.application.coworking_booking.persistence.entity.PaymentEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import com.coworking.application.coworking_booking.persistence.repository.spring.PaymentJpaRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final com.coworking.application.coworking_booking.business.service.AdminService service;
  private final BookingJpaRepository bookingRepo;
  private final PaymentJpaRepository paymentRepo;

  public AdminController(
      com.coworking.application.coworking_booking.business.service.AdminService s,
      BookingJpaRepository b,
      PaymentJpaRepository p
  ) {
    this.service = s;
    this.bookingRepo = b;
    this.paymentRepo = p;
  }

  //  AUDITS 

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

 

  //  REPORTS

  @GetMapping("/reports/bookings")
  @Operation(summary = "Reporte de reservas", description = "Devuelve todas las reservas con detalles")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> bookings() {
    try {
      List<Map<String, Object>> out = bookingRepo.findAll().stream()
          .map(this::mapBooking)
          .toList();
      return ResponseEntity.ok(out);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  private Map<String, Object> mapBooking(BookingEntity b) {
    Map<String, Object> m = new LinkedHashMap<>();
    var user = b.getUser();
    var space = b.getSpace();
    m.put("bookingId", b.getId());
    m.put("userId", user != null ? user.getId() : null);
    m.put("userEmail", user != null ? user.getEmail() : null);
    m.put("spaceId", space != null ? space.getId() : null);
    m.put("spaceName", space != null ? space.getName() : null);
    m.put("startTime", b.getStartTime());
    m.put("endTime", b.getEndTime());
    m.put("status", b.getBookingStatus() != null ? b.getBookingStatus().name() : null);
    m.put("totalAmount", b.getTotalAmount());
    return m;
  }

  @GetMapping("/reports/revenue")
  @Operation(
      summary = "Reporte de ingresos",
      description = "Lista de usuarios con espacios y el total pagado (agregado a partir de pagos existentes)"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> revenue() {
    try {
      List<PaymentEntity> payments = paymentRepo.findAll();

      // Agrupar por (userId, userEmail, spaceId, spaceName)
      Map<RevenueKey, java.math.BigDecimal> totals = payments.stream()
          .collect(Collectors.groupingBy(
              p -> {
                var b = p.getBooking();
                Long uId = (b != null && b.getUser() != null) ? b.getUser().getId() : null;
                String uMail = (b != null && b.getUser() != null) ? b.getUser().getEmail() : null;
                Long sId = (b != null && b.getSpace() != null) ? b.getSpace().getId() : null;
                String sName = (b != null && b.getSpace() != null) ? b.getSpace().getName() : null;
                return new RevenueKey(uId, uMail, sId, sName);
              },
              Collectors.reducing(java.math.BigDecimal.ZERO, PaymentEntity::getAmount, java.math.BigDecimal::add)
          ));

      List<Map<String, Object>> out = totals.entrySet().stream().map(e -> {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("userId", e.getKey().userId());
        m.put("userEmail", e.getKey().userEmail());
        m.put("spaceId", e.getKey().spaceId());
        m.put("spaceName", e.getKey().spaceName());
        m.put("totalPaid", e.getValue());
        return m;
      }).sorted(Comparator
          .comparing((Map<String, Object> m) -> (Long) m.get("userId"), Comparator.nullsLast(Long::compareTo))
          .thenComparing(m -> (Long) m.get("spaceId"), Comparator.nullsLast(Long::compareTo))
      ).toList();

      return ResponseEntity.ok(out);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  // Helper record para la clave del agregado de revenue
  private record RevenueKey(Long userId, String userEmail, Long spaceId, String spaceName) {}
}
