//package com.ptc.sendgrid.config;
//
//import com.sendgrid.SendGrid;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SendGridConfig {
//
//  @Value("$spring.sendgrid.key")
//  private String sendGridKey;
//
//  @Bean
//  public SendGrid getSendGridId() {
//    return new SendGrid(sendGridKey);
//  }
//}
