package com.ptc.sendgrid.service;

import com.ptc.sendgrid.domain.Notification;

public interface NotificationService {

  Notification sendNotification(Notification notification);

}
