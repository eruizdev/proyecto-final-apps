package main.java.com.coworking.application.coworking_booking.presentation.controller;

import com.coworking.application.coworking_booking.business.service.BookingService;
import com.coworking.application.coworking_booking.presentation.dto.booking.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

  public BookingController(BookingService s) {
    this.service = s;
  }

  // Solo USER puede crear reservas
  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingCreateRequestDTO req) {
    var b = service.create(req.userId(), req.spaceId(), req.startTime(), req.endTime(), req.attendees());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BookingResponseDTO(
            b.getId(),
            b.getBookingStatus().name(),
            b.getStartTime(),
            b.getEndTime(),
            b.getAttendees(),
            b.getTotalAmount()
        ));
  }
}