package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.BookingService;
import com.coworking.application.coworking_booking.presentation.dto.booking.BookingCreateRequestDTO;
import com.coworking.application.coworking_booking.presentation.dto.booking.BookingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService service;

  public BookingController(BookingService s) {
    this.service = s;
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  @Operation(summary = "Crear reserva", description = "Crea una nueva reserva")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Creado"),
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> create(@RequestBody BookingCreateRequestDTO req) {
    try {
      var b = service.create(
          req.userId(), req.spaceId(), req.startTime(), req.endTime(), req.attendees()
      );
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new BookingResponseDTO(
              b.getId(), b.getBookingStatus().name(),
              b.getStartTime(), b.getEndTime(),
              b.getAttendees(), b.getTotalAmount()
          ));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{id}")
  @Operation(summary = "Obtener reserva", description = "Devuelve una reserva por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> get(@PathVariable Long id) {
    try {
      var b = service.get(id);
      return ResponseEntity.ok(new BookingResponseDTO(
          b.getId(), b.getBookingStatus().name(),
          b.getStartTime(), b.getEndTime(),
          b.getAttendees(), b.getTotalAmount()
      ));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Cancelar reserva", description = "Cancela una reserva por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminado"),
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> cancel(@PathVariable Long id) {
    try {
      service.cancel(id);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/user/{userId}")
  @Operation(summary = "Reservas por usuario", description = "Lista todas las reservas de un usuario por su ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> byUser(@PathVariable Long userId) {
    try {
      final int INTERNAL_LIMIT = 50;
      List<BookingResponseDTO> list = service.listByUser(userId, INTERNAL_LIMIT).stream()
          .map(b -> new BookingResponseDTO(
              b.getId(), b.getBookingStatus().name(),
              b.getStartTime(), b.getEndTime(),
              b.getAttendees(), b.getTotalAmount()
          ))
          .toList();
      return ResponseEntity.ok(list);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
