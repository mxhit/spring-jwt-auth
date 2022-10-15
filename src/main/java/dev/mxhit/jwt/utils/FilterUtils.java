package dev.mxhit.jwt.utils;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import dev.mxhit.jwt.role.Role;
import dev.mxhit.jwt.user.User;

public class FilterUtils {
	public static Algorithm getAlgorithm() {
		return Algorithm.HMAC256("secret".getBytes()); // Replace this with actual secret
	}
	
	public static String createAccessToken(HttpServletRequest request, UserDetails user) {
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // expiration time is 10 minutes
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(getAlgorithm());
		
		return accessToken;
	}
	
	public static String createAccessToken(HttpServletRequest request, User user) {
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // expiration time is 10 minutes
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",
						user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
				.sign(getAlgorithm());
		
		return accessToken;
	}
	
	public static String createRefreshToken(HttpServletRequest request, UserDetails user) {
		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // expiration time is 30 minutes
				.withIssuer(request.getRequestURL().toString()).sign(getAlgorithm());
		
		return refreshToken;
	}
	
	public static DecodedJWT verifyToken(String token) {
		// Building verifier to verify token
		JWTVerifier verifier = JWT.require(getAlgorithm()).build();
		
		// Verifying the token and returning the decoded value
		DecodedJWT decodedJWT = verifier.verify(token);
		
		return decodedJWT;
	}
}
