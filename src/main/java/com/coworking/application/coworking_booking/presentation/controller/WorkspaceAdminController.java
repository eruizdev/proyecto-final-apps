package main.java.com.coworking.application.coworking_booking.presentation.controller;

// importes
import com.coworking.application.coworking_booking.business.service.AdminWorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceTypeUpsertDTO;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceUpsertDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para la gestion de espacios (solo ADMIN)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WorkspaceAdminController {
    
private final AdminWorkspaceService service;
 public WorkspaceAdminController(AdminWorkspaceService s){ this.service = s; }

  // Crear un nuevo espacio
  @PostMapping("/space-types")
  public ResponseEntity<SpaceTypeEntity> createType(@RequestBody SpaceTypeUpsertDTO dto){
    var t = SpaceTypeEntity.builder()
        .name(dto.name()).description(dto.description())
        .basePricePerHour(dto.basePricePerHour())
        .amenities(dto.amenities())
        .active(dto.active() != null ? dto.active() : true)
        .build();
    return ResponseEntity.ok(service.createType(t));
  }
}