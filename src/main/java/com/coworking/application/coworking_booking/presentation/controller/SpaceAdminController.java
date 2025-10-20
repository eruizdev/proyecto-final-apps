package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.UserFineEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceBasicUpsertDTO;
import com.coworking.application.coworking_booking.presentation.dto.workspace.FineCreateDTO;
import com.coworking.application.coworking_booking.presentation.dto.workspace.FineResponseDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/admin/spaces-basic")
@PreAuthorize("hasRole('ADMIN')")
public class SpaceAdminController {

  private final SpaceJpaRepository spaces;
  private final SpaceTypeJpaRepository types;

  // NUEVO: repos de usuarios y multas
  private final UserJpaRepository users;
  private final UserFineJpaRepository fines;

  public SpaceAdminController(
      SpaceJpaRepository s,
      SpaceTypeJpaRepository t,
      UserJpaRepository users,
      UserFineJpaRepository fines) {
    this.spaces = s;
    this.types = t;
    this.users = users;
    this.fines = fines;
  }

 

  @PostMapping
  @Operation(summary = "Crear espacio (básico)", description = "Crea un espacio con mapa simple")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Creado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "Tipo de espacio no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> create(@RequestBody SpaceBasicUpsertDTO body) {
    try {
      var now = LocalDateTime.now();
      var space = SpaceEntity.builder()
          .spaceType(types.findById(body.getSpaceTypeId()).orElseThrow())
          .name(body.getName())
          .capacity(body.getCapacity())
          .pricePerHour(body.getPricePerHour())
          .spaceStatus(SpaceEntity.SpaceStatus.valueOf(body.getStatus()))
          .active(true)
          .createdAt(now)
          .updatedAt(now)
          .build();
      space = spaces.save(space);
      return ResponseEntity.ok(Map.of("id", space.getId(), "name", space.getName()));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SpaceType no encontrado");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar espacio (básico)", description = "Actualiza un espacio con mapa simple")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Actualizado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "Espacio no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody SpaceBasicUpsertDTO body) {
    try {
      var s = spaces.findById(id).orElseThrow();
      if (body.getName() != null) s.setName(body.getName());
      if (body.getCapacity() != null) s.setCapacity(body.getCapacity());
      if (body.getPricePerHour() != null) s.setPricePerHour(body.getPricePerHour());
      if (body.getStatus() != null) s.setSpaceStatus(SpaceEntity.SpaceStatus.valueOf(body.getStatus()));
      s.setUpdatedAt(LocalDateTime.now());
      spaces.save(s);
      return ResponseEntity.ok(Map.of("id", s.getId(), "updated", true));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Espacio no encontrado");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar espacio (básico)", description = "Elimina un espacio por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminado"),
      @ApiResponse(responseCode = "404", description = "Espacio no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    try {
      spaces.deleteById(id); // elimina solo por ID
      return ResponseEntity.noContent().build(); // 204
    } catch (EmptyResultDataAccessException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Espacio no encontrado"); // 404 si no existe
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  // ------------------ ENDPOINTS: MULTAS ------------------

  @PostMapping("/fines")
  @Operation(summary = "Crear multa para un usuario",
             description = "Crea una deuda/multa indicando id de usuario, motivo y precio. Solo ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Creado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> createFine(@RequestBody FineCreateDTO req) {
    try {
      if (req.getUserId() == null || req.getAmount() == null || req.getAmount() <= 0
          || req.getReason() == null || req.getReason().isBlank()) {
        return ResponseEntity.badRequest().body("userId, reason y amount (>0) son obligatorios");
      }

      var user = users.findById(req.getUserId())
          .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

      var fine = UserFineEntity.builder()
          .user(user)
          .reason(req.getReason())
          .amount(req.getAmount())
          .createdAt(LocalDateTime.now())
          .build();

      fine = fines.save(fine);

      var out = FineResponseDTO.builder()
          .id(fine.getId())
          .userId(user.getId())
          .userEmail(user.getEmail())
          .reason(fine.getReason())
          .amount(fine.getAmount())
          .createdAt(fine.getCreatedAt())
          .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(out);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @GetMapping("/fines")
  @Operation(summary = "Listar multas",
             description = "Devuelve todas las multas/deudas con todos sus detalles. Solo ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> listFines() {
    try {
      var out = fines.findAllByOrderByCreatedAtDesc().stream().map(f ->
          FineResponseDTO.builder()
              .id(f.getId())
              .userId(f.getUser().getId())
              .userEmail(f.getUser().getEmail())
              .reason(f.getReason())
              .amount(f.getAmount())
              .createdAt(f.getCreatedAt())
              .build()
      ).toList();

      return ResponseEntity.ok(out);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
