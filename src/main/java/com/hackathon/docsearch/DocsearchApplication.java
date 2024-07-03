package com.hackathon.docsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DocsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsearchApplication.class, args);
	}

}
