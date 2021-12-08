package com.ptc.sendgrid.controller;

import com.ptc.sendgrid.api.gen.NotificationsApi;
import com.ptc.sendgrid.api.gen.model.CreateNotificationRequest;
import com.ptc.sendgrid.api.gen.model.CreateNotificationResponse;
import com.ptc.sendgrid.domain.Notification;
import com.ptc.sendgrid.mapper.NotificationMapper;
import com.ptc.sendgrid.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationsApi {

  private final NotificationService notificationService;
  private static final NotificationMapper NOTIFICATION_MAPPER = NotificationMapper.INSTANCE;

  @Override
  public ResponseEntity<CreateNotificationResponse> sendNotification(
      final CreateNotificationRequest createNotificationRequest) {
    final Notification notification = NOTIFICATION_MAPPER.toNotification(createNotificationRequest);
    notificationService.sendNotification(notification);
    return null;
  }

}
