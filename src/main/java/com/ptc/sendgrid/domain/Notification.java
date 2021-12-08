package com.ptc.sendgrid.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

  @Id
  private String id;

  private List<Account> toEmails;

  private List<Account> ccEmails;

  private List<Account> bccEmails;

  private String message;

  private NotificationType type;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @CreatedDate
  private LocalDateTime createdAt;

  @CreatedBy
  private String createdBy;

  @LastModifiedBy
  private String updatedBy;

}
