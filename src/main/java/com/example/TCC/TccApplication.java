package com.example.TCC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TccApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccApplication.class, args);
	}

}
