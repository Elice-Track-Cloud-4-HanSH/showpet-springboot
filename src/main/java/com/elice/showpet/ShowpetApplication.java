package com.elice.showpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableJdbcAuditing
public class ShowpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowpetApplication.class, args);
	}

}
