package com.example.postsmith_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PostsmithApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostsmithApiApplication.class, args);
	}

}
