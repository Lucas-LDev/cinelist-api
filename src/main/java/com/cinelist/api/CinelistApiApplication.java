package com.cinelist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.cinelist")
@EnableCaching
public class CinelistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinelistApiApplication.class, args);
	}

}
