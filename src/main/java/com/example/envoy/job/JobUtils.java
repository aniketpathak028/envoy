package com.example.envoy.job;

import com.example.envoy.dto.EmailScheduleRequest;
import com.google.gson.Gson;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class JobUtils {

    public JobDetail buildJobDetail(EmailScheduleRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("to", scheduleEmailRequest.getTo());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        // convert from string array to string
        if(scheduleEmailRequest.getCc() != null) {
            String ccString = new Gson().toJson(scheduleEmailRequest.getCc());
            jobDataMap.put("cc", ccString);
        }

        if(scheduleEmailRequest.getBcc() != null) {
            String bccString = new Gson().toJson(scheduleEmailRequest.getBcc());
            jobDataMap.put("bcc", bccString);
        }

        if(scheduleEmailRequest.getTrackEmail() != null) {
            jobDataMap.put("trackEmail", scheduleEmailRequest.getTrackEmail());
        }

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
