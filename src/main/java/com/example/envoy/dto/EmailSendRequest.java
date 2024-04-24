package com.example.envoy.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailSendRequest {

        @Email
        @NotEmpty
        private String to;

        @NotEmpty
        private String subject;

        @NotEmpty
        private String body;

        private List<@Email String> cc;

        private List<@Email String> bcc;

        @Email
        private String trackEmail;

}
