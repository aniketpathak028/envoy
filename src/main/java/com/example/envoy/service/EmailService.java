package com.example.envoy.service;

import com.example.envoy.dto.EmailResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface EmailService {
    void sendMail(String fromEmail, String toEmail, String subject, String body, String[] cc, String[] bcc, String trackEmail) throws MessagingException;
    void trackMail(String uniqueIdentifier);
}
