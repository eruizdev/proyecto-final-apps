package com.coworking.application.coworking_booking.presentation.dto.subscription;

import java.math.BigDecimal;

public record SubscriptionPlanCreateDTO(
    String type,
    BigDecimal price
) {}