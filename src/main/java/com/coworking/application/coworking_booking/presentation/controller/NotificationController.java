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
    
    private final NotificationService service;
    private final NotificationJpaRepository repo;

  public NotificationController(NotificationService service, NotificationJpaRepository repo) {
    this.service = service;
    this.repo = repo;
  }

  /**
   *  Solo usuarios autenticados pueden ver sus notificaciones no leídas.
   * Se usa el userId del token (o del path, según prefieras).
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/unread/{userId}")
  public List<?> unread(@PathVariable Long userId) {
    return service.unread(userId).stream().map(n -> java.util.Map.of(
        "id", n.getId(),
        "title", n.getTitle(),
        "message", n.getMessage(),
        "type", n.getNotificationType().name(),
        "createdAt", n.getCreatedAt()
    )).toList();
  }
}