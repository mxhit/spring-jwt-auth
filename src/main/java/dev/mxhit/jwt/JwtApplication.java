package dev.mxhit.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Bean
	Logger logger() {
		return LoggerFactory.getLogger(JwtApplication.class);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	/*
	 * @Bean CommandLineRunner runner(UserService userService) { return args -> {
	 * final String ROLE_ADMIN = "ROLE_ADMIN"; final String ROLE_USER = "ROLE_USER";
	 * 
	 * userService.saveRole(new Role(null, ROLE_ADMIN)); userService.saveRole(new
	 * Role(null, ROLE_USER));
	 * 
	 * userService.saveUser(new User(null, "Mohit", "Dodhia", "mxhit",
	 * "testPwd@123", new ArrayList<>(), false));
	 * 
	 * userService.addRoleToUser("mxhit", ROLE_ADMIN); }; }
	 */
	 
}
