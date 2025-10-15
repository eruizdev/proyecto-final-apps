// DTO para la creacion y actualizacion de tipos de espacios

package main.java.com.coworking.application.coworking_booking.presentation.dto.workspace;

import java.math.BigDecimal;

public record SpaceTypeUpsertDTO(
    String name,
    String description,
    BigDecimal basePricePerHour,
    String amenities,
    Boolean active
) {}
