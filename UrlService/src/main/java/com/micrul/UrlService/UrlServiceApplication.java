package com.micrul.UrlService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(UrlServiceApplication.class, args);
	}

}
