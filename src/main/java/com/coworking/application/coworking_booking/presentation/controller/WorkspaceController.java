package main.java.com.coworking.application.coworking_booking.presentation.controller;

// importes
import com.coworking.application.coworking_booking.business.service.WorkspaceService;
import com.coworking.application.coworking_booking.presentation.dto.workspace.WorkspaceResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para la gestion de espacios (ADMIN y USER)
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService service;

  public WorkspaceController(WorkspaceService s) {
    this.service = s;
  }

  // Solo ADMIN puede listar todos los espacios
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public List<WorkspaceResponseDTO> list() {
    return service.listActives().stream().map(s ->
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
  }

  
}