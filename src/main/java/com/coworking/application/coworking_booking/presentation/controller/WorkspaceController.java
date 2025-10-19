package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.exception.BusinessException;
import com.coworking.application.coworking_booking.business.exception.NotFoundException;
import com.coworking.application.coworking_booking.business.service.WorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.workspace.WorkspaceResponseDTO;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

  private final WorkspaceService service;
  private final SpaceJpaRepository spaceRepo;

  public WorkspaceController(WorkspaceService s, SpaceJpaRepository r) {
    this.service = s;
    this.spaceRepo = r;
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping
  @Operation(summary = "Listar espacios (filtro opcional)", description = "Lista espacios activos o filtrados")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> list(@RequestParam(required = false) Long typeId,
                                @RequestParam(required = false) Integer capacityMin,
                                @RequestParam(required = false) String status) {
    try {
      if (typeId == null && capacityMin == null && status == null) {
        var body = service.listActives().stream().map(s ->
            new WorkspaceResponseDTO(
                s.getId(),
                s.getName(),
                s.getSpaceType().getName(),
                s.getCapacity(),
                s.getPricePerHour(),
                s.getSpaceStatus().name(),
                s.getLocation()
            )
        ).toList();
        return ResponseEntity.ok(body);
      }

      SpaceEntity.SpaceStatus st = status == null ? null : SpaceEntity.SpaceStatus.valueOf(status);
      var list = spaceRepo.search(typeId, capacityMin, st);
      var body = list.stream().map(s ->
          new WorkspaceResponseDTO(
              s.getId(),
              s.getName(),
              s.getSpaceType().getName(),
              s.getCapacity(),
              s.getPricePerHour(),
              s.getSpaceStatus().name(),
              s.getLocation()
          )
      ).toList();
      return ResponseEntity.ok(body);
    } catch (IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{id}")
  @Operation(summary = "Detalle de espacio", description = "Devuelve un espacio por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "No encontrado"),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor")
  })
  public ResponseEntity<?> get(@PathVariable Long id) {
    try {
      var s = service.get(id);
      return ResponseEntity.ok(new WorkspaceResponseDTO(
          s.getId(),
          s.getName(),
          s.getSpaceType().getName(),
          s.getCapacity(),
          s.getPricePerHour(),
          s.getSpaceStatus().name(),
          s.getLocation()
      ));
    } catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
  }
}
