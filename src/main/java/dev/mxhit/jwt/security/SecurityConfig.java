package dev.mxhit.jwt.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dev.mxhit.jwt.filter.CustomAuthenticationFilter;
import dev.mxhit.jwt.filter.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// Disabling CSRF
			.csrf().disable()
			
			// Removing sessions
			.sessionManagement().sessionCreationPolicy(STATELESS)
			.and().authorizeHttpRequests().antMatchers("/api/user/token/refresh").permitAll()
			
			// User endpoints
			.and().authorizeHttpRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
			.and().authorizeHttpRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN")
			
			.and().authorizeHttpRequests().anyRequest().authenticated()
			
			// Implementing a custom filter to take care of authorization using OncePerRequestFilter
			.and().addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			
			// Implementing a custom filter to take care of authentication using a custom authentication manager
			.addFilter(new CustomAuthenticationFilter(new CustomAuthenticationManager(userDetailsService, passwordEncoder)));

		return http.build();
	}
}
