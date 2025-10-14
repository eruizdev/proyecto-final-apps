// DTO para la creacion de reservas response

package main.java.com.coworking.application.coworking_booking.presentation.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponseDTO(Long id, String status,
                                 LocalDateTime startTime, LocalDateTime endTime,
                                 Integer attendees, BigDecimal totalAmount) {}
