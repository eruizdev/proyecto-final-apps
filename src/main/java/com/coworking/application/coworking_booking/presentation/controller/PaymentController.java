package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.persistence.repository.spring.PaymentJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Muestra las MULTAS (fines) del usuario.
 */
@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class PaymentController {

  private final PaymentJpaRepository repo;

  public PaymentController(PaymentJpaRepository r) {
    this.repo = r;
  }

  @GetMapping("/user/{userId}")
  @Operation(
      summary = "Multas por usuario",
      description = "Devuelve únicamente las multas asignadas al usuario (por su ID). Requiere token."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> finesByUser(@PathVariable Long userId) {
    try {
      // Traemos todos los pagos del usuario, luego filtramos SOLO multas.
      var all = repo.findByBooking_User_IdOrderByPaymentDateDesc(userId);

     
      var fines = all.stream()
          .filter(p ->
              (p.getPaymentStatus() != null && "FINE".equalsIgnoreCase(p.getPaymentStatus().name())) ||
              (p.getPaymentMethod() != null && "FINE".equalsIgnoreCase(p.getPaymentMethod()))
          )
          .map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", p.getId());
            m.put("userId", p.getBooking().getUser().getId());
            m.put("bookingId", p.getBooking().getId());
            // Concepto/razón de la multa: si no tienes un campo específico,
            // usamos paymentMethod como concepto, puedes renombrar si corresponde.
            m.put("concept", p.getPaymentMethod());
            m.put("amount", p.getAmount());
            m.put("status", p.getPaymentStatus() != null ? p.getPaymentStatus().name() : null);
            m.put("date", p.getPaymentDate());
            return m;
          })
          .toList();

     
      // if (fines.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay multas para el usuario.");
      return ResponseEntity.ok(fines);

    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
