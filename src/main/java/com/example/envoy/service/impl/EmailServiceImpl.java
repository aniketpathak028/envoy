package com.example.envoy.service.impl;

import com.example.envoy.dto.EmailResponse;
import com.example.envoy.model.EmailTrackRequest;
import com.example.envoy.repository.EmailTrackRepository;
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
import java.time.format.DateTimeFormatter;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;


@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTrackRepository emailTrackRepository;

    @Override
    public ResponseEntity<EmailResponse> sendMail(String fromEmail, String toEmail, String subject, String body, String[] cc, String[] bcc, String trackEmail) {
        try {
            logger.info("Sending Email to {}", toEmail);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());

            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setCc(cc);
            messageHelper.setBcc(bcc);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);

            if(trackEmail!=null){
                String uniqueIdentifier = generateUniqueIdentifier(
                        new EmailTrackRequest(toEmail, trackEmail, subject)
                );

                String trackingPixelUrl = "https://spring-email-production.up.railway.app/mail/track?id=" + uniqueIdentifier;
                //String trackingPixelUrl = "http://localhost:8081/mail/track?id=emailUnga";
                String trackedBody = body + "<img src=\"" + trackingPixelUrl + "\">";
                messageHelper.setText(trackedBody, true);
            }

            logger.info("Before time: {}", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            mailSender.send(message);
            logger.info("After time: {}", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            EmailResponse scheduleEmailResponse = new EmailResponse(true, "Email sent successfully");
            return ResponseEntity.ok(scheduleEmailResponse);

        } catch (MessagingException ex) {
            logger.error("Failed to send email to {}", toEmail);

            EmailResponse scheduleEmailResponse = new EmailResponse(false,
                    "Error sending email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }

    @Override
    public void trackMail(String uniqueIdentifier) {
        //call db with the identifier.
        //get the email ids
        //send email
        //subject: Your email is opened.
        //body: Your email with subject "subject" to "to" email is being opened at "time".
    }

    private String generateUniqueIdentifier(EmailTrackRequest etr) {
        EmailTrackRequest savedEntity = emailTrackRepository.save(etr);
        return Long.toString(savedEntity.getId());
    }
}
