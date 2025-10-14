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

  // Listar todos los tipos de espacio 
  @PutMapping("/space-types/{id}")
  public ResponseEntity<SpaceTypeEntity> updateType(@PathVariable Long id, @RequestBody SpaceTypeUpsertDTO dto){
    var t = SpaceTypeEntity.builder()
        .name(dto.name()).description(dto.description())
        .basePricePerHour(dto.basePricePerHour())
        .amenities(dto.amenities())
        .active(dto.active())
        .build();
    return ResponseEntity.ok(service.updateType(id, t));
  }

  // Metodo para listar todos los tipos de espacio
  @DeleteMapping("/space-types/{id}")
  public ResponseEntity<Void> deleteType(@PathVariable Long id){
    service.deleteType(id);
    return ResponseEntity.noContent().build();
  }

  // Listar todos los tipos de espacio
  @GetMapping("/space-types")
  public List<SpaceTypeEntity> listTypes(){ return service.listTypes(); }

  // Crear un nuevo espacio
  @PostMapping("/spaces")
  public ResponseEntity<SpaceEntity> createSpace(@RequestBody SpaceUpsertDTO dto){
    var s = SpaceEntity.builder()
        .name(dto.name()).description(dto.description())
        .capacity(dto.capacity())
        .pricePerHour(dto.pricePerHour())
        .spaceStatus(dto.status()!=null ? SpaceEntity.SpaceStatus.valueOf(dto.status()) : null)
        .location(dto.location()).equipment(dto.equipment()).images(dto.images())
        .active(dto.active()!=null ? dto.active() : true)
        .build();
    return ResponseEntity.ok(service.createSpace(s, dto.spaceTypeId()));
  }

  // Actualizar un espacio existente
  @PutMapping("/spaces/{id}")
  public ResponseEntity<SpaceEntity> updateSpace(@PathVariable Long id, @RequestBody SpaceUpsertDTO dto){
    var s = SpaceEntity.builder()
        .name(dto.name()).description(dto.description())
        .capacity(dto.capacity())
        .pricePerHour(dto.pricePerHour())
        .spaceStatus(dto.status()!=null ? SpaceEntity.SpaceStatus.valueOf(dto.status()) : null)
        .location(dto.location()).equipment(dto.equipment()).images(dto.images())
        .active(dto.active()!=null ? dto.active() : true)
        .build();
    return ResponseEntity.ok(service.updateSpace(id, s, dto.spaceTypeId()));
  }

  // Eliminar un espacio
  @DeleteMapping("/spaces/{id}")
  public ResponseEntity<Void> deleteSpace(@PathVariable Long id){
    service.deleteSpace(id);
    return ResponseEntity.noContent().build();
  }

  // Listar todos los espacios
  
  @GetMapping("/spaces")
  public List<SpaceEntity> listSpaces(){ return service.listSpaces(); }

}