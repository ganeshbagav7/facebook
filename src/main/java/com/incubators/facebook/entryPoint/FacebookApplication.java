													// श्री //
package com.incubators.facebook.entryPoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(value = "com.incubators.facebook.data.repository")
@EntityScan("com.incubators.facebook.data.model")
@ComponentScan(value = {"com.incubators.facebook.controller","com.incubators.facebook.filter","com.incubators.facebook.service"})
@SpringBootApplication
public class FacebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacebookApplication.class, args);
	}

}

