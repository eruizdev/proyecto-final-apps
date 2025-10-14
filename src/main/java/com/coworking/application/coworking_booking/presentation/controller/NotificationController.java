package main.java.com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import com.coworking.application.coworking_booking.persistence.repository.spring.NotificationJpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
}