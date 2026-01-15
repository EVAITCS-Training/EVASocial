package com.horrorcore.eva_social.services;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.horrorcore.eva_social.config.JwtConfigProperty;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Autowired
	private JwtConfigProperty jwtConfigProperty;
	
	@Autowired
	private UserCredentialRepository userCredentialRepository;
	
	
	public String generateToken(String username) {
		UserCredential userCredential = userCredentialRepository.findById(username)
				.orElseThrow(() -> 
				new UsernameNotFoundException("User with email of " 
				+ username 
				+ " not found"));
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", userCredential.getEmail());
		claims.put("role", userCredential.getRole());
		return generateToken(claims, userCredential);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public boolean validateToken(String token, UserCredential userCredential) {
		final String usernameString = extractUsername(token);
		return (usernameString.equalsIgnoreCase(userCredential.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	private String generateToken(Map<String, Object> claims, UserCredential userCredential) {
		return Jwts
				.builder()
				.claims(claims)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + Duration.ofHours(1).toMillis()))
				.signWith(getSecretKey())
				.compact();
	}
	
	private SecretKey getSecretKey() {
		byte[] encodedKey = Decoders.BASE64.decode(jwtConfigProperty.getSecret());
		return Keys.hmacShaKeyFor(encodedKey);
	}

}
