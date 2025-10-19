package com.coworking.application.coworking_booking.presentation.dto.workspace;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "SpaceBasicUpsertDTO", description = "Payload básico para crear/actualizar un Space")
public class SpaceBasicUpsertDTO {

  @Schema(description = "ID del tipo de espacio", example = "1")
  private Long spaceTypeId;

  @Schema(description = "Nombre del espacio", example = "Sala 101")
  private String name;

  @Schema(description = "Capacidad (personas)", example = "8")
  private Integer capacity;

  @Schema(description = "Precio por hora", example = "30000.00")
  private BigDecimal pricePerHour;

  @Schema(description = "Estado (AVAILABLE, OCCUPIED, MAINTENANCE)", example = "AVAILABLE")
  private String status;

  public Long getSpaceTypeId() { return spaceTypeId; }
  public void setSpaceTypeId(Long spaceTypeId) { this.spaceTypeId = spaceTypeId; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getCapacity() { return capacity; }
  public void setCapacity(Integer capacity) { this.capacity = capacity; }

  public BigDecimal getPricePerHour() { return pricePerHour; }
  public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
