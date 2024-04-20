package com.example.envoy.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "track_mail")
public class EmailTrackRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "to_email")
    String to;

    @Column(name = "track_email")
    String trackEmail;

    @Column(name = "subject")
    String subject;

    public EmailTrackRequest(String toEmail, String trackEmail, String subject) {
        this.to = toEmail;
        this.trackEmail = trackEmail;
        this.subject = subject;
    }

}
