package com.example.Mech_App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MechAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechAppApplication.class, args);
	}

}
