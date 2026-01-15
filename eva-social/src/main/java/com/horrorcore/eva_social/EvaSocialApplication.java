package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class EvaSocialApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@Autowired
	private UserProfileService userProfileService;

	public static void main(String[] args) {
		SpringApplication.run(EvaSocialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create admin user if not exists
		if (!userCredentialRepository.existsById("admin@horrorcore.com")) {
			UserCredential admin = UserCredential.builder()
					.email("admin@horrorcore.com")
					.password(passwordEncoder.encode("Gudmord92!"))
					.role("ADMIN")
					.build();
			userCredentialRepository.save(admin);
			
			// Create profile for admin
			userProfileService.createProfile(admin);
			
			System.out.println("Admin user created successfully!");
		}
	}
}
