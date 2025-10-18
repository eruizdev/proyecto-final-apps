package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.persistence.repository.spring.PaymentJpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class PaymentController {

  private final PaymentJpaRepository repo;

  public PaymentController(PaymentJpaRepository r) { this.repo = r; }

  @GetMapping("/user/{userId}")
  public List<Map<String, Object>> byUser(@PathVariable Long userId) {
    List<Map<String, Object>> out = new ArrayList<>();
    repo.findByBooking_User_IdOrderByPaymentDateDesc(userId).forEach(p -> {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("id", p.getId());
      m.put("bookingId", p.getBooking().getId());
      m.put("amount", p.getAmount());
      m.put("status", p.getPaymentStatus().name());
      m.put("method", p.getPaymentMethod());
      m.put("date", p.getPaymentDate());
      out.add(m);
    });
    return out;
  }
}
