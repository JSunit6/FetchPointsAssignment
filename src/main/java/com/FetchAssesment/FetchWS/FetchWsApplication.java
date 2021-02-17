package com.FetchAssesment.FetchWS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"Controller", "Exceptions", "Model", "Repository"})
//@EnableJpaRepositories("Repository")
@EntityScan("Model")
public class FetchWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchWsApplication.class, args);
	}

}
