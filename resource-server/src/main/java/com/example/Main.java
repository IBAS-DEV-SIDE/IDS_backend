package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.spoti.api.auth"})
public class Main {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
}
