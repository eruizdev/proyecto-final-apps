package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.persistence.repository.spring.PaymentJpaRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class PaymentController {

  private final PaymentJpaRepository repo;

  public PaymentController(PaymentJpaRepository r) { this.repo = r; }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Pagos por usuario", description = "Lista pagos de un usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> byUser(@PathVariable Long userId) {
    try {
      List<Map<String, Object>> out = new ArrayList<>();
      repo.findByBooking_User_IdOrderByPaymentDateDesc(userId).forEach(p -> {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("bookingId", p.getBooking().getId());
        m.put("amount", p.getAmount());
        m.put("status", p.getPaymentStatus().name());
        m.put("method", p.getPaymentMethod());
        m.put("date", p.getPaymentDate());
        out.add(m);
      });
      return ResponseEntity.ok(out);
    } catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
