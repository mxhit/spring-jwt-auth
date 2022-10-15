package dev.mxhit.jwt.user;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mxhit.jwt.utils.FilterUtils;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
	@Autowired
	Logger log;

	@Autowired
	UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("Calling endpoint /user/all");

		return ResponseEntity.ok().body(userService.getAllUsers());
	}

	@PostMapping("/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		log.info("Calling endpoint /user/save");

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());

		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}

	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		log.info("Calling endpoint /user/token/refresh");

		String authorizationHeader = request.getHeader(AUTHORIZATION);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				// Fetching access token
				String refreshToken = authorizationHeader.substring("Bearer ".length());

				DecodedJWT decodedJWT = FilterUtils.verifyToken(refreshToken);

				String username = decodedJWT.getSubject();

				User user = userService.getUser(username);

				// Creating access token
				String accessToken = FilterUtils.createAccessToken(request, user);

				// Adding tokens in response body
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);

				// Setting content type of response to be application/json
				response.setContentType(APPLICATION_JSON_VALUE);

				// Passing the tokens as response body
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
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
			throw new RuntimeException("Refresh token in missing");
		}
	}
}
