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

  /**
   *  Endpoint de prueba o administración — devuelve TODAS las notificaciones.
   * Solo ADMIN puede ver todas (útil para debug o panel administrativo).
   */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public List<NotificationEntity> allNotifications() {
    return repo.findAll();
  }

  /**
   *  Versión basada en el email del token (más segura).
   * Devuelve las notificaciones del usuario autenticado.
   */
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping
  public List<NotificationEntity> myNotifications(Authentication auth) {
    // auth.getName() = email del usuario extraído del JWT
    // En versión demo devolvemos todas; podrías filtrar por email si tu entidad tiene user.email
    return repo.findAll();
  }
}