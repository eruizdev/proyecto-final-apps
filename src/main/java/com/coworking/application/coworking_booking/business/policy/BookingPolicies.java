package com.coworking.application.coworking_booking.business.policy;

import com.coworking.application.coworking_booking.business.exception.ValidationException;
import com.coworking.application.coworking_booking.persistence.entity.SpaceEntity;

import java.time.*;

public class BookingPolicies {

  public void validateCreation(SpaceEntity space, LocalDateTime start, LocalDateTime end,
                               int attendees, boolean hasOverlap, int futureCount) {
    if (!start.isBefore(end)) throw new ValidationException("Rango inválido (inicio>=fin)");
    long hours = Duration.between(start, end).toHours();
    if (hours < 1 || hours > 8) throw new ValidationException("Reserva mínima 1h y máxima 8h");
    if (Duration.between(LocalDateTime.now(), start).toDays() > 30)
      throw new ValidationException("No más de 30 días de anticipación");
    if (Duration.between(LocalDateTime.now(), start).toHours() < 2)
      throw new ValidationException("Crear/modificar mínimo con 2 horas");
    if (attendees > space.getCapacity()) throw new ValidationException("Excede capacidad");
    if (hasOverlap) throw new ValidationException("Horario no disponible");
    if (futureCount >= 5) throw new ValidationException("Máximo 5 reservas futuras");
    if (space.getSpaceStatus() != SpaceEntity.SpaceStatus.AVAILABLE)
      throw new ValidationException("Espacio no disponible");
  }

  public boolean penalize50(LocalDateTime start, LocalDateTime now){
    return Duration.between(now, start).toHours() < 24;
  }
}

