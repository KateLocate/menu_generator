package com.katelocate.menugenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MenuGeneratorApplication {

	private static final Logger logger = LoggerFactory.getLogger(MenuGeneratorApplication.class);

	@EnableWebSecurity
	public class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/**").permitAll()
				.anyRequest().authenticated();
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(MenuGeneratorApplication.class, args);
	}
}
