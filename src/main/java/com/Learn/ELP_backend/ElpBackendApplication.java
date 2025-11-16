package com.Learn.ELP_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class ElpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElpBackendApplication.class, args);
	}

}
