package com.horrorcore.eva_social.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.horrorcore.eva_social.dto.AuthRequest;
import com.horrorcore.eva_social.dto.AuthResponse;
import com.horrorcore.eva_social.dto.PostNewUser;
import com.horrorcore.eva_social.services.JwtService;
import com.horrorcore.eva_social.services.UserCredentialService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserCredentialService userCredentialService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody PostNewUser postNewUser) {
		userCredentialService.register(postNewUser);
		return ResponseEntity.created(null).build();
		}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authRequest.username(), authRequest.password()));
		String jwtString = jwtService.generateToken(authRequest.username());
		return ResponseEntity.ok(new AuthResponse(jwtString));
	}
}
