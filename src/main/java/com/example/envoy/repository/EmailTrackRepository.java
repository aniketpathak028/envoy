package com.example.envoy.repository;

import com.example.envoy.model.EmailTrackRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTrackRepository extends JpaRepository<EmailTrackRequest, Long> {
}
