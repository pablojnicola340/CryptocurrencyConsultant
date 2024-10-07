package com.exercise.criptocurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableR2dbcRepositories("com.exercise.criptocurrency.repository")
@EntityScan(basePackages = "com.exercise.criptocurrency.entitites")
@EnableFeignClients(basePackages = "com.exercise.criptocurrency.restClient")
public class CriptocurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CriptocurrencyApplication.class, args);
	}

}
