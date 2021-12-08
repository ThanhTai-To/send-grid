package com.ptc.sendgrid.service.impl;

import com.ptc.sendgrid.domain.Account;
import com.ptc.sendgrid.domain.Notification;
import com.ptc.sendgrid.service.NotificationService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.ClickTrackingSetting;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.OpenTrackingSetting;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sendgrid.helpers.mail.objects.SubscriptionTrackingSetting;
import com.sendgrid.helpers.mail.objects.TrackingSettings;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  private SendGrid sendGrid;

  @Value("${app.sendgrid.from-email}")
  private String fromEmail;

  @Value("${app.sendgrid.from-name:#{''}}")
  private String fromName;

  @Value("${app.sendgrid.tracking-setting.substitution-tag}")
  private String substitutionTag;

  @Override
  public Notification sendNotification(final Notification notification) {
    try {
      sendEmail(notification);
    } catch (IOException exception) {
      if (log.isErrorEnabled()) {
        log.error(notification.toString());
      }
    }
    return null;
  }

  private void sendEmail(final Notification notification) throws IOException {

    final String subject = "Subject test integrate sendgrid";

    if (CollectionUtils.isNotEmpty(notification.getToEmails())) {
      final Mail mail = createMail(notification, subject);
      final Request request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());

      final Response response = sendGrid.api(request);
      if (log.isInfoEnabled()) {
        log.info("Status code: [{}], body: [{}], headers: [{}]",
            response.getStatusCode(),
            response.getBody(),
            response.getHeaders());
      }
    }
  }

  private Mail createMail(final Notification notification, final String subject) {
    //TODO: validate email unique in bbc,cc,to
    // Email template
    // Email header, footer

    final Mail mail = new Mail();

    final Personalization personalization = new Personalization();

    this.setPersonalizationToEmails(notification.getToEmails(), personalization);
    this.setPersonalizationBccEmails(notification.getBccEmails(), personalization);
    this.setPersonalizationCcEmails(notification.getCcEmails(), personalization);

    mail.addPersonalization(personalization);
    mail.setFrom(this.createEmail(fromEmail, fromName));
    mail.setReplyTo(this.createEmail(fromEmail, fromName));
    mail.setSubject(subject);

    mail.addCategory("Activate account");

    final String contentType = "text/html";
    final String contentValue = "<p>Hello from Twilio SendGrid!</p>"
        + "<p>Sending with the email service trusted by developers and marketers for <strong>time-savings</strong>, <strong>scalability</strong>, and <strong>delivery expertise</strong>.</p>"
        + "<p>%open-track%</p>";
    this.setMailContent(mail, contentType, contentValue);

    final String attachmentContent = "PCFET0NUWVBFIGh0bWw+CjxodG1sIGxhbmc9ImVuIj4KCiAgICA8aGVhZD4KICAgICAgICA8bWV0YSBjaGFyc2V0PSJVVEYtOCI+CiAgICAgICAgPG1ldGEgaHR0cC1lcXVpdj0iWC1VQS1Db21wYXRpYmxlIiBjb250ZW50PSJJRT1lZGdlIj4KICAgICAgICA8bWV0YSBuYW1lPSJ2aWV3cG9ydCIgY29udGVudD0id2lkdGg9ZGV2aWNlLXdpZHRoLCBpbml0aWFsLXNjYWxlPTEuMCI+CiAgICAgICAgPHRpdGxlPkRvY3VtZW50PC90aXRsZT4KICAgIDwvaGVhZD4KCiAgICA8Ym9keT4KCiAgICA8L2JvZHk+Cgo8L2h0bWw+Cg==";
    final String fileName = "index.html";
    final String attachmentType = "text/html";
    final String disposition = "attachment";
    this.addAttachment(mail, attachmentContent, fileName, attachmentType, disposition);

    this.setTrackingSetting(mail);

    return mail;
  }

  private Email createEmail(final String email, final String name) {
    if (StringUtils.isNotBlank(name)) {
      return new Email(email, name);
    }
    return new Email(email);
  }

  private void setPersonalizationToEmails(
      final List<Account> accounts,
      final Personalization personalization) {
    if (CollectionUtils.isNotEmpty(accounts)) {
      accounts.forEach(account -> personalization
          .addTo(this.createEmail(account.getEmail(), account.getName())));
    }
  }

  private void setPersonalizationBccEmails(
      final List<Account> accounts,
      final Personalization personalization) {
    if (CollectionUtils.isNotEmpty(accounts)) {
      accounts.forEach(account -> personalization
          .addBcc(this.createEmail(account.getEmail(), account.getName())));
    }
  }

  private void setPersonalizationCcEmails(
      final List<Account> accounts,
      final Personalization personalization) {
    if (CollectionUtils.isNotEmpty(accounts)) {
      accounts.forEach(account -> personalization
          .addCc(this.createEmail(account.getEmail(), account.getName())));
    }
  }

  private void setMailContent(
      final Mail mail,
      final String contentType,
      final String contentValue) {
    final Content content = new Content();
    content.setType(contentType);
    content.setValue(contentValue);
    mail.addContent(content);
  }

  private void addAttachment(
      final Mail mail,
      final String attachmentContent,
      final String fileName,
      final String attachmentType,
      final String disposition) {
    final Attachments attachment = new Attachments();
    attachment.setContent(attachmentContent);
    attachment.setFilename(fileName);
    attachment.setType(attachmentType);
    attachment.setDisposition(disposition);
    mail.addAttachments(attachment);
  }

  private void setTrackingSetting(final Mail mail) {
    final TrackingSettings trackingSettings = new TrackingSettings();
    final ClickTrackingSetting clickTrackingSetting = new ClickTrackingSetting();
    clickTrackingSetting.setEnable(true);
    clickTrackingSetting.setEnableText(false);
    trackingSettings.setClickTrackingSetting(clickTrackingSetting);
    final OpenTrackingSetting openTrackingSetting = new OpenTrackingSetting();
    openTrackingSetting.setEnable(true);
    openTrackingSetting.setSubstitutionTag(substitutionTag);
    trackingSettings.setOpenTrackingSetting(openTrackingSetting);
    final SubscriptionTrackingSetting subscriptionTrackingSetting = new SubscriptionTrackingSetting();
    subscriptionTrackingSetting.setEnable(false);
    trackingSettings.setSubscriptionTrackingSetting(subscriptionTrackingSetting);
    mail.setTrackingSettings(trackingSettings);
  }

}
