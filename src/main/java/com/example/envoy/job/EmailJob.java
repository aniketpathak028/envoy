package com.example.envoy.job;

import com.example.envoy.service.EmailService;
import com.google.gson.Gson;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String to = jobDataMap.getString("to");

        String[] ccArray = new String[0];
        if(jobDataMap.containsKey("cc")){
            ccArray= new Gson().fromJson(jobDataMap.getString("cc"),String[].class);
        }

        String[] bccArray = new String[0];
        if(jobDataMap.containsKey("bcc")){
            bccArray= new Gson().fromJson(jobDataMap.getString("bcc"),String[].class);
        }

        emailService.sendMail(mailProperties.getUsername(), to, subject, body, ccArray, bccArray);

    }
}
