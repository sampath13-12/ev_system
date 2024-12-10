package com.ev_centers.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvCentersBillingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvCentersBillingSystemApplication.class, args);
		System.out.println("Ev billing system started");
	}

}
