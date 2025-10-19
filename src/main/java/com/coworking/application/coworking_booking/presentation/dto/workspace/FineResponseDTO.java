package com.coworking.application.coworking_booking.presentation.dto.workspace;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FineResponseDTO {
  private Long id;
  private Long userId;
  private String userEmail;
  private String reason;
  private Integer amount;
  private LocalDateTime createdAt;
}
