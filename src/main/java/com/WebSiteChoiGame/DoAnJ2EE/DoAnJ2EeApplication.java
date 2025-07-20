package com.WebSiteChoiGame.DoAnJ2EE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.WebSiteChoiGame.DoAnJ2EE.repository")
public class DoAnJ2EeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoAnJ2EeApplication.class, args);
	}

}
