package com.example.abhijournalwebapp.journalWebApplication;

/*
A "journal in press" refers to a research article that has been accepted for publication
in a scholarly journal but has not yet been officially published, meaning it is currently
undergoing the final formatting and production stages before being released in a specific
issue, and therefore may not have complete publication details like volume, issue, or page
numbers assigned yet
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//Enable Scheduling Process In Spring Boot App:
@EnableScheduling
public class JournalWebAppilcation {
	public static void main(String[] args) {
		SpringApplication.run(JournalWebAppilcation.class, args);
	}

	//Creating an instance/implementation of RestTemplate To avoid errors:
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
