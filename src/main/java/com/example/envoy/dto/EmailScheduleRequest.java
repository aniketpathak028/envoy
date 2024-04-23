package com.example.envoy.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
public class EmailScheduleRequest {
    @Email
    @NotEmpty
    private String to;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private ZoneId timeZone;

    private List<@Email String> cc;

    private List<@Email String> bcc;

    @Email
    private String trackEmail;
}
