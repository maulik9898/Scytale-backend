package com.scytale.backend;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;
import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.service.UserService;
import com.scytale.backend.authentication.utility.Constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;



@SpringBootApplication
public class ScytaleBackendApplication {

	@Value("${env.JWT_SECRET}")
	public String JWT_SECRET;

	@Value("${env.ADMIN_USERNAME}")
	private String ADMIN_USERNAME;

	@Value("${env.ADMIN_NAME}")
	private String ADMIN_NAME;

	@Value("${env.ADMIN_PASSWORD}")
	private String ADMIN_PASSWORD;


	public static void main(String[] args) {
		SpringApplication.run(ScytaleBackendApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	CommandLineRunner run(UserService userService ){
		return args -> {
			new Constants().setJwtSecret(JWT_SECRET);
			if (!userService.existRole(ERole.ROLE_USER))
				userService.saveRole(new Role(null, ERole.ROLE_USER));
			if (!userService.existRole(ERole.ROLE_ADMIN))
				userService.saveRole(new Role(null, ERole.ROLE_ADMIN));

			if (userService.userExist(this.ADMIN_USERNAME)) {
				userService.deleteUser(this.ADMIN_USERNAME);
			}
			User user = userService.saveUser(new User(null, this.ADMIN_NAME, this.ADMIN_USERNAME, this.ADMIN_PASSWORD, new ArrayList<>()));


			userService.addRoleToUser(user.getUsername(), ERole.ROLE_ADMIN);
		};
	}
}
