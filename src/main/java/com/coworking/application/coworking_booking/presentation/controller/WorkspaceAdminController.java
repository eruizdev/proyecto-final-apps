package main.java.com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.AdminWorkspaceService;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;
import com.coworking.application.coworking_booking.persistence.entity.SpaceTypeEntity;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceTypeUpsertDTO;
import com.coworking.application.coworking_booking.presentation.dto.workspace.SpaceUpsertDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WorkspaceAdminController {
}