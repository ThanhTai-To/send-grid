package com.ptc.sendgrid.mapper;

import com.ptc.sendgrid.api.gen.model.CreateNotificationRequest;
import com.ptc.sendgrid.api.gen.model.CreateNotificationResponse;
import com.ptc.sendgrid.domain.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

  NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

  @Mapping(target = "id", ignore = true)
  Notification toNotification(CreateNotificationRequest createNotificationRequest);

  CreateNotificationResponse toCreateNotificationResponse(Notification notification);

}
