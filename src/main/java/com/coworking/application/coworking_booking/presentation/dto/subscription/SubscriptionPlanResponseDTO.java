package com.coworking.application.coworking_booking.presentation.dto.subscription;

import java.math.BigDecimal;

public record SubscriptionPlanResponseDTO(
    Long id,
    String type,
    BigDecimal price,
    boolean active
) {}