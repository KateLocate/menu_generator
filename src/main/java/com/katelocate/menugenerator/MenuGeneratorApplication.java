package com.katelocate.menugenerator;

import com.katelocate.menugenerator.recepie.Recepie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MenuGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(MenuGeneratorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MenuGeneratorApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			Recepie recepie = new Recepie(1, "Boiled Eggs",
					"Put an egg in a pot with cold water so that it covers an egg, wait until the water boils. " +
							"Simmer for 8 minutes. Cool it down with running water in a sink.");
            logger.info("Recepie created");
		};
	};
}
