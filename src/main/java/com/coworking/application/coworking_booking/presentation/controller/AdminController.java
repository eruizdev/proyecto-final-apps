package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.repository.spring.BookingJpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final com.coworking.application.coworking_booking.business.service.AdminService service;
  private final BookingJpaRepository bookingRepo;

  public AdminController(
      com.coworking.application.coworking_booking.business.service.AdminService s,
      BookingJpaRepository b
  ) {
    this.service = s;
    this.bookingRepo = b;
  }

  @GetMapping("/audits")
  public List<Map<String, Object>> auditsAll() {
    return service.auditsAll().stream().map(a -> {
      Map<String, Object> m = new HashMap<>();
      m.put("id", a.getId());
      m.put("userId", a.getUser().getId());
      m.put("entityType", a.getEntityType());
      m.put("entityId", a.getEntityId());
      m.put("action", a.getAction());
      m.put("createdAt", a.getCreatedAt());
      return m;
    }).toList();
  }

  @GetMapping("/audits/user/{userId}")
  public List<Map<String, Object>> auditsByUser(@PathVariable Long userId) {
    return service.auditsByUser(userId).stream().map(a -> {
      Map<String, Object> m = new HashMap<>();
      m.put("id", a.getId());
      m.put("userId", a.getUser().getId());
      m.put("entityType", a.getEntityType());
      m.put("entityId", a.getEntityId());
      m.put("action", a.getAction());
      m.put("createdAt", a.getCreatedAt());
      return m;
    }).toList();
  }

  @GetMapping("/audits/entity/{entityType}")
  public List<Map<String, Object>> auditsByEntityType(@PathVariable String entityType,
                                                      @RequestParam(defaultValue = "50") int limit) {
    return service.auditsByEntityType(entityType.toUpperCase(), limit).stream().map(a -> {
      Map<String, Object> m = new HashMap<>();
      m.put("id", a.getId());
      m.put("userId", a.getUser().getId());
      m.put("entityType", a.getEntityType());
      m.put("entityId", a.getEntityId());
      m.put("action", a.getAction());
      m.put("createdAt", a.getCreatedAt());
      return m;
    }).toList();
  }

  @GetMapping("/reports/bookings")
  public Map<String, Object> bookings(@RequestParam String from,
                                      @RequestParam String to,
                                      @RequestParam(required = false) Long spaceId) {
    var f = LocalDateTime.parse(from);
    var t = LocalDateTime.parse(to);
    return Map.of("count", bookingRepo.countBookings(f, t, spaceId));
  }

  @GetMapping("/reports/revenue")
  public Map<String, Object> revenue(@RequestParam String from,
                                     @RequestParam String to) {
    var f = LocalDateTime.parse(from);
    var t = LocalDateTime.parse(to);
    return Map.of("revenue", bookingRepo.sumRevenue(f, t));
  }
}
