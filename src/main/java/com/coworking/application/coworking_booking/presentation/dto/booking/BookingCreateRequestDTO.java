// DTO para la creacion de reservas

package main.java.com.coworking.application.coworking_booking.presentation.dto.booking;

import java.time.LocalDateTime;

public record BookingCreateRequestDTO(Long userId, Long spaceId,
                                      LocalDateTime startTime, LocalDateTime endTime,
                                      Integer attendees) {}
