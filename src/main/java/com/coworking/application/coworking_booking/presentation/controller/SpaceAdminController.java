package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.*;
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

  public SpaceAdminController(SpaceJpaRepository s, SpaceTypeJpaRepository t) {
    this.spaces = s;
    this.types = t;
  }

  @PostMapping
  @Operation(summary = "Crear espacio (básico)", description = "Crea un espacio con mapa simple")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Creado"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "Tipo de espacio no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
    try {
      var now = LocalDateTime.now();
      var space = SpaceEntity.builder()
          .spaceType(types.findById(Long.valueOf(body.get("spaceTypeId").toString())).orElseThrow())
          .name(body.get("name").toString())
          .capacity(Integer.valueOf(body.get("capacity").toString()))
          .pricePerHour(new java.math.BigDecimal(body.get("pricePerHour").toString()))
          .spaceStatus(SpaceEntity.SpaceStatus.valueOf(body.get("status").toString()))
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
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
    try {
      var s = spaces.findById(id).orElseThrow();
      if (body.containsKey("name")) s.setName(body.get("name").toString());
      if (body.containsKey("capacity")) s.setCapacity(Integer.valueOf(body.get("capacity").toString()));
      if (body.containsKey("pricePerHour")) s.setPricePerHour(new java.math.BigDecimal(body.get("pricePerHour").toString()));
      if (body.containsKey("status")) s.setSpaceStatus(SpaceEntity.SpaceStatus.valueOf(body.get("status").toString()));
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
      @ApiResponse(responseCode = "200", description = "Eliminado"),
      @ApiResponse(responseCode = "404", description = "Espacio no encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      spaces.deleteById(id);
      return ResponseEntity.ok(Map.of("id", id, "deleted", true));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
