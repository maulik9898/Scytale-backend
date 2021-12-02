package com.scytale.backend;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;
import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ScytaleBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScytaleBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService ){
		return args -> {
			userService.saveRole(new Role(null, ERole.ROLE_ADMIN));
			userService.saveRole(new Role(null,ERole.ROLE_USER));

			User user = userService.saveUser(new User(null,"Maulik","maulik9898","12345678",new ArrayList<>()));

			userService.saveUser(new User(null,"Maulik User","hhhh","12345678",new ArrayList<>()));

			userService.addRoleToUser(user.getUsername(),ERole.ROLE_ADMIN);
		};
	}
}
