package com.katelocate.menugenerator;

import com.katelocate.menugenerator.user.User;
import com.katelocate.menugenerator.user.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class MenuGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(MenuGeneratorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MenuGeneratorApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(UserRestClient client) {
//		return args -> {
//			List<User> users = client.findall();
//			System.out.println(users);
//		};
//	}
}
