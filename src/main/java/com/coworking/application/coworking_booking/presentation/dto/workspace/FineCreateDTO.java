package com.coworking.application.coworking_booking.presentation.dto.workspace;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FineCreateDTO {
  private Long userId;     // id del usuario
  private String reason;   // "MULTA POR: ..."
  private Integer amount;  // PRECIO de la multa
}
