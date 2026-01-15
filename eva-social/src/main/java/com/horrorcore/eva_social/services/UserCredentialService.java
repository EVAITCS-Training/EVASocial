package com.horrorcore.eva_social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.horrorcore.eva_social.dto.PostNewUser;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserCredentialService {
	@Autowired
	private UserCredentialRepository userCredentialRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional(rollbackFor = Exception.class)
	public void register(PostNewUser postNewUser) {

		if (userCredentialRepository.existsById(postNewUser.getEmail().toLowerCase().trim())) {
			throw new IllegalArgumentException("Email already exists");
		}

		UserCredential userCredential = UserCredential
				.builder()
				.email(postNewUser.getEmail().toLowerCase().trim())
				.password(passwordEncoder.encode(postNewUser.getPassword()))
				.role("USER")
				.build();
		userCredentialRepository.save(userCredential);
	}
}
