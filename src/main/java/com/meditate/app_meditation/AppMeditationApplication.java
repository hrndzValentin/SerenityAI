package com.meditate.app_meditation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.retry.annotation.EnableRetry
@org.springframework.scheduling.annotation.EnableScheduling
@org.springframework.data.jpa.repository.config.EnableJpaRepositories
public class AppMeditationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppMeditationApplication.class, args);
	}

}
