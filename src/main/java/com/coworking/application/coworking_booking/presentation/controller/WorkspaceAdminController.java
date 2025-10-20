package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.AdminWorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceTypeUpsertDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WorkspaceAdminController {

  private final AdminWorkspaceService service;

  public WorkspaceAdminController(AdminWorkspaceService s) {
    this.service = s;
  }

  // ---------------------- SPACE TYPES ----------------------

  @PostMapping("/space-types")
  @Operation(summary = "Crear tipo de espacio", description = "Crea un SpaceType")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> createType(@RequestBody SpaceTypeUpsertDTO dto) {
    try {
      var t = SpaceTypeEntity.builder()
          .name(dto.name())
          .description(dto.description())
          .basePricePerHour(dto.basePricePerHour())
          .amenities(dto.amenities())
          .active(dto.active() != null ? dto.active() : true)
          .build();
      return ResponseEntity.ok(service.createType(t));
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }

  @PutMapping("/space-types/{id}")
  @Operation(summary = "Actualizar tipo de espacio", description = "Actualiza un SpaceType")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> updateType(@PathVariable Long id, @RequestBody SpaceTypeUpsertDTO dto) {
    try {
      var t = SpaceTypeEntity.builder()
          .name(dto.name())
          .description(dto.description())
          .basePricePerHour(dto.basePricePerHour())
          .amenities(dto.amenities())
          .active(dto.active())
          .build();
      return ResponseEntity.ok(service.updateType(id, t));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (BusinessException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }

  @DeleteMapping("/space-types/{id}")
  @Operation(summary = "Eliminar tipo de espacio", description = "Borra un SpaceType por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Eliminado"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> deleteType(@PathVariable Long id) {
    try {
      service.deleteType(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }

  @GetMapping("/space-types")
  @Operation(summary = "Listar tipos de espacio", description = "Devuelve SpaceTypes")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> listTypes() {
    try {
      List<SpaceTypeEntity> list = service.listTypes();
      return ResponseEntity.ok(list);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error interno del servidor");
    }
  }
}
