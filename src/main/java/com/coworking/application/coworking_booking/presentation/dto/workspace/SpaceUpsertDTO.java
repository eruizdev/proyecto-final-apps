// Dto para la actualizacion y creacion de espacios

package com.coworking.application.coworking_booking.presentation.dto.workspace;

import java.math.BigDecimal;

public record SpaceUpsertDTO(
    Long spaceTypeId,
    String name,
    String description,
    Integer capacity,
    BigDecimal pricePerHour,
    String status,     // "AVAILABLE" | "OCCUPIED" | "MAINTENANCE"
    String location,
    String equipment,  // JSON string opcional
    String images,     // JSON string opcional
    Boolean active
) {}
