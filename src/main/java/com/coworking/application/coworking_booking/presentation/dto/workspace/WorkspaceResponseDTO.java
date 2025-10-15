// DTO para la respuesta de espacios

package main.java.com.coworking.application.coworking_booking.presentation.dto.workspace;

import java.math.BigDecimal;

public record WorkspaceResponseDTO(Long id, String name, String typeName,
                                   Integer capacity, BigDecimal pricePerHour,
                                   String status, String location) {}
