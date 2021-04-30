package com.bug.influx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfluxApplication.class, args);
	}
}
