package dev.mxhit.jwt.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationManager implements AuthenticationManager {
	@Autowired
	Logger log;
	
	UserDetailsService userDetailsService;
	
	PasswordEncoder passwordEncoder;
	

	/**
	 * @param userDetailsService
	 * @param passwordEncoder
	 */
	public CustomAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}



	/**
	 * Overriding authenticate method to implement custom authentication checks
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

		if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			log.warn("Password entered was incorrect");

			throw new BadCredentialsException("Wrong password");
		}

		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
	}

}
