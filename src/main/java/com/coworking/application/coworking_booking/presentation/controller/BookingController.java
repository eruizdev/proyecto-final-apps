package com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.BookingService;
import com.coworking.application.coworking_booking.persistence.entity.BookingEntity;
import com.coworking.application.coworking_booking.presentation.dto.booking.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/bookings")
public class BookingController {
  private final BookingService service;
  public BookingController(BookingService s){ this.service = s; }

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingCreateRequestDTO req){
    var b = service.create(req.userId(), req.spaceId(), req.startTime(), req.endTime(), req.attendees());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BookingResponseDTO(b.getId(), b.getBookingStatus().name(),
            b.getStartTime(), b.getEndTime(), b.getAttendees(), b.getTotalAmount()));
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{id}")
  public BookingResponseDTO get(@PathVariable Long id){
    var b = service.get(id);
    return new BookingResponseDTO(b.getId(), b.getBookingStatus().name(),
        b.getStartTime(), b.getEndTime(), b.getAttendees(), b.getTotalAmount());
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancel(@PathVariable Long id){
    service.cancel(id);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/user/{userId}")
  public List<BookingResponseDTO> byUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue="50") int limit){
    return service.listByUser(userId, limit).stream().map(b ->
        new BookingResponseDTO(b.getId(), b.getBookingStatus().name(),
            b.getStartTime(), b.getEndTime(), b.getAttendees(), b.getTotalAmount())
    ).toList();
  }
}
