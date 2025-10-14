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
}