package dev.mxhit.jwt.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mxhit.jwt.utils.FilterUtils;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
	@Autowired
	Logger log;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Not doing anything as /login is an open endpoint
		if (request.getServletPath().equals("/login") || request.getServletPath().equals("/api/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					// Fetching access token
					String token = authorizationHeader.substring("Bearer ".length());

					DecodedJWT decodedJWT = FilterUtils.verifyToken(token);

					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

					Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

					// Generating authenticated token which Spring can use in the application
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, authorities);

					// Passing the authenticated token to Application Context
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

					filterChain.doFilter(request, response);
				} catch (Exception e) {
					log.error("Error logging in: {}", e.getMessage());

					response.setHeader("error", e.getMessage());
					response.setStatus(FORBIDDEN.value());

					// Adding error in response body
					Map<String, String> errors = new HashMap<>();
					errors.put("error_message", e.getMessage());

					// Setting content type of response to be application/json
					response.setContentType(APPLICATION_JSON_VALUE);

					// Passing the errors as response body
					new ObjectMapper().writeValue(response.getOutputStream(), errors);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
