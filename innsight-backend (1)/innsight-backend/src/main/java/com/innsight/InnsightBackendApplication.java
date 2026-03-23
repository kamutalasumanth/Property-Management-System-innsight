package com.innsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.innsight")
public class InnsightBackendApplication {

	public static void main(String[] args) {

        SpringApplication.run(InnsightBackendApplication.class, args);
	}

}
