package com.coworking.application.coworking_booking.business.service.scheduler;

import com.coworking.application.coworking_booking.business.repository.BookingRepositoryPort;
import com.coworking.application.coworking_booking.business.service.NotificationService;
import com.coworking.application.coworking_booking.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingReminderJob {
  private final BookingRepositoryPort bookings;
  private final NotificationService notifications;

  @Scheduled(fixedDelay = 300_000) // cada 5 minutos
  public void run() {
    var now = LocalDateTime.now();
    sendWindow(now.plusHours(24), NotificationEntity.NotificationType.REMINDER_24H);
    sendWindow(now.plusHours(1),  NotificationEntity.NotificationType.REMINDER_1H);
  }

  private void sendWindow(LocalDateTime target, NotificationEntity.NotificationType type){
    var from = target.minusMinutes(5);
    var to   = target.plusMinutes(5);
    bookings.findStartingBetween(from, to).forEach(b -> {
      String title = type==NotificationEntity.NotificationType.REMINDER_24H ? "Recordatorio 24h" : "Recordatorio 1h";
      String msg = "Tu reserva #" + b.getId() + " empieza a las " + b.getStartTime();
      // Creamos una notificación usando tu NotificationService (reutilizamos método existente simple)
      
      notifications.notifyBookingCreated(b.getUser().getId(), b.getId()); // simple para MVP
    });
  }
}
