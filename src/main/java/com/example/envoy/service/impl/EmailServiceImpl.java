package com.example.envoy.service.impl;

import com.example.envoy.model.EmailTrackRequest;
import com.example.envoy.repository.EmailTrackRepository;
import com.example.envoy.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Optional;


@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTrackRepository emailTrackRepository;

    @Async
    @Override
    public void sendMail(String fromEmail, String toEmail, String subject, String body, String[] cc, String[] bcc, String trackEmail) throws MessagingException {

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
                        new EmailTrackRequest(toEmail, trackEmail, subject, false)
                );

                String trackingPixelUrl = "https://envoy-api.up.railway.app/api/v1/track?id=" + uniqueIdentifier;
                String trackedBody = body + "<img src=\"" + trackingPixelUrl + "\">";
                messageHelper.setText(trackedBody, true);
            }

            mailSender.send(message);
    }

    @Override
    public void trackMail(String uniqueIdentifier) {
        Optional<EmailTrackRequest> dbEntry = emailTrackRepository.findById(Long.parseLong(uniqueIdentifier));
        dbEntry.ifPresent(entry -> {
            if(!entry.getIsOpened()){
                String trackingEmail = entry.getTrackEmail();
                String body = entry.getTo() + " just opened your email with the subject '" + entry.getSubject() +"'."+ "<br/>" + "Read on " + LocalTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"));
                try {
                    sendMail(mailProperties.getUsername(), trackingEmail,"Your email was just read", body, new String[0] , new String[0], null);
                    entry.setIsOpened(true);
                    emailTrackRepository.save(entry);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private String generateUniqueIdentifier(EmailTrackRequest etr) {
        EmailTrackRequest savedEntity = emailTrackRepository.save(etr);
        return Long.toString(savedEntity.getId());
    }
}
