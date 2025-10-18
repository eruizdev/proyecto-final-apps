package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.WorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.SpaceJpaRepository;
import com.coworking.application.coworking_booking.presentation.dto.workspace.WorkspaceResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para la gestion de espacios (ADMIN y USER)
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService service;
    private final SpaceJpaRepository spaceRepo; // 🔹 añadido

    public WorkspaceController(WorkspaceService s, SpaceJpaRepository r) {
        this.service = s;
        this.spaceRepo = r;
    }

    // 🔹 Fusionado: permite filtrar pero mantiene la versión admin original
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<WorkspaceResponseDTO> list(@RequestParam(required = false) Long typeId,
                                           @RequestParam(required = false) Integer capacityMin,
                                           @RequestParam(required = false) String status) {

        if (typeId == null && capacityMin == null && status == null) {
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

        SpaceEntity.SpaceStatus st = status == null ? null : SpaceEntity.SpaceStatus.valueOf(status);
        var list = spaceRepo.search(typeId, capacityMin, st);
        return list.stream().map(s ->
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

    // ADMIN o USER pueden ver detalles de un espacio
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public WorkspaceResponseDTO get(@PathVariable Long id) {
        var s = service.get(id);
        return new WorkspaceResponseDTO(
                s.getId(),
                s.getName(),
                s.getSpaceType().getName(),
                s.getCapacity(),
                s.getPricePerHour(),
                s.getSpaceStatus().name(),
                s.getLocation()
        );
    }
}
