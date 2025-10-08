package com.cinelist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cinelist")
public class CinelistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinelistApiApplication.class, args);
	}

}
