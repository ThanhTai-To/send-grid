package com.ptc.sendgrid;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongock
@EnableMongoAuditing
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class SendGridApplication {

  @Autowired
  private SendGrid sendGrid;

  public static void main(final String[] args) {
    SpringApplication.run(SendGridApplication.class, args);
  }

}
