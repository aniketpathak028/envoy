package com.example.envoy.controller;

import com.example.envoy.dto.EmailScheduleRequest;
import com.example.envoy.dto.EmailResponse;
import com.example.envoy.dto.EmailSendRequest;
import com.example.envoy.job.JobUtils;
import com.example.envoy.service.EmailService;
import jakarta.validation.Valid;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobUtils jobUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailProperties mailProperties;

    @GetMapping("/")
    public String homePage(){
        return "Welcome to Envoy!";
    }


    @PostMapping("/api/v1/schedule")
    public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailScheduleRequest scheduleEmailRequest) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), scheduleEmailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                EmailResponse scheduleEmailResponse = new EmailResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }

            JobDetail jobDetail = jobUtils.buildJobDetail(scheduleEmailRequest);
            Trigger trigger = jobUtils.buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            EmailResponse scheduleEmailResponse = new EmailResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email.", ex);

            EmailResponse scheduleEmailResponse = new EmailResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }

    @PostMapping("/api/v1/send")
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailSendRequest sendEmailRequest) {
        String[] ccArray = new String[0];
        if(sendEmailRequest.getCc()!= null){
            ccArray= sendEmailRequest.getCc().toArray(new String[0]);
        }

        String[] bccArray = new String[0];
        if(sendEmailRequest.getBcc()!= null){
            bccArray= sendEmailRequest.getBcc().toArray(new String[0]);
        }

        try{
            emailService.sendMail(
                    mailProperties.getUsername(),
                    sendEmailRequest.getTo(),
                    sendEmailRequest.getSubject(),
                    sendEmailRequest.getBody(),
                    ccArray,
                    bccArray,
                    sendEmailRequest.getTrackEmail()
            );
            EmailResponse sendEmailResponse = new EmailResponse(true, "Email sent successfully");
            return ResponseEntity.ok(sendEmailResponse);
        } catch(Exception e){
            EmailResponse sendEmailResponse = new EmailResponse(false,
                    "Error sending email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sendEmailResponse);
        }
    }

    @GetMapping("/api/v1/track")
    public ResponseEntity<byte[]> getImage(@RequestParam("id") String uniqueIdentifier) throws IOException {
        String imageUrl = "https://api-envoy.s3.ap-south-1.amazonaws.com/tracking.png"; //should be changed later

        emailService.trackMail(uniqueIdentifier);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok().contentType(response.getHeaders().getContentType()).body(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }
}
