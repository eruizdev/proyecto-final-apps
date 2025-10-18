package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.SubscriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/subscriptions")
@PreAuthorize("hasRole('USER')")
public class SubscriptionController {
  private final SubscriptionService service;
  public SubscriptionController(SubscriptionService s){ this.service = s; }

  @PostMapping
  public java.util.Map<String,Object> activate(@RequestParam Long userId, @RequestParam Long planId){
    var sub = service.activate(userId, planId);
    return java.util.Map.of("id", sub.getId(), "userId", userId, "plan", sub.getPlan().getName(), "active", sub.isActive());
  }

  @GetMapping("/me")
  public java.util.Map<String,Object> me(@RequestParam Long userId){
    var s = service.me(userId);
    return s==null ? java.util.Map.of("active", false) :
        java.util.Map.of("active", true, "plan", s.getPlan().getName(), "startAt", s.getStartAt());
  }
}
