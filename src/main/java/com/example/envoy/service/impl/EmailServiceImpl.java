package com.example.envoy.service.impl;

import com.example.envoy.controller.EmailController;
import com.example.envoy.dto.EmailResponse;
import com.example.envoy.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public ResponseEntity<EmailResponse> sendMail(String fromEmail, String toEmail, String subject, String body, String[] cc, String[] bcc) {
        try {
            logger.info("Sending Email to {}", toEmail);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setCc(cc);
            messageHelper.setBcc(bcc);

            mailSender.send(message);

            EmailResponse scheduleEmailResponse = new EmailResponse(true, "Email sent successfully");
            return ResponseEntity.ok(scheduleEmailResponse);

        } catch (MessagingException ex) {
            logger.error("Failed to send email to {}", toEmail);

            EmailResponse scheduleEmailResponse = new EmailResponse(false,
                    "Error sending email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }
}
