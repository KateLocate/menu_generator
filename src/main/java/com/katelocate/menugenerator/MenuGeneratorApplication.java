package com.katelocate.menugenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MenuGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(MenuGeneratorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MenuGeneratorApplication.class, args);
	}

}
