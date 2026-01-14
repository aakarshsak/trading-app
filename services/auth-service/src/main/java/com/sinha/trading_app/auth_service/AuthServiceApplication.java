package com.sinha.trading_app.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.util.TimeZone;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.sinha.trading_app.common", "com.sinha.trading_app.auth_service"})
@PropertySource("classpath:.env")
public class AuthServiceApplication {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
