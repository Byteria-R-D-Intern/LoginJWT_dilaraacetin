package com.example.loginjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class LoginjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginjwtApplication.class, args);
	}

}
