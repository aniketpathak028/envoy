package com.example.envoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EnvoyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnvoyApplication.class, args);
	}

}
