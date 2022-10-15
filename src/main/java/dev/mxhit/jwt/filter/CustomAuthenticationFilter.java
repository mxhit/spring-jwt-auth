package dev.mxhit.jwt.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mxhit.jwt.security.CustomAuthenticationManager;
import dev.mxhit.jwt.utils.FilterUtils;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	Logger log;
	
	CustomAuthenticationManager authenticationManager;
	
	
	/**
	 * @param authenticationManager
	 */
	public CustomAuthenticationFilter(CustomAuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * This method is invoked when authentication is attempted
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

//		log.info("Attempting authentication with username: {}", username);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		return authenticationManager.authenticate(authenticationToken);
	}

	/**
	 * This method is invoked when authentication is successful
	 * It is also responsible for generating JWT access and refresh tokens
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {

		// User object from Spring's UserDetails package
		User user = new User(authentication.getPrincipal().toString(), authentication.getCredentials().toString(),
				authentication.getAuthorities());

		// Creating access token
		String accessToken = FilterUtils.createAccessToken(request, user);

		// Creating refresh token
		String refreshToken = FilterUtils.createRefreshToken(request, user);

		// Adding tokens in response body
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		
		// Setting content type of response to be application/json
		response.setContentType(APPLICATION_JSON_VALUE);
		
		// Passing the tokens as response body
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
}
