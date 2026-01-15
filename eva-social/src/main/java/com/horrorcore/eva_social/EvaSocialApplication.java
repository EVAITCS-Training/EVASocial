package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
public class EvaSocialApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private PostRepository postRepository;

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

		// Create dummy test users and posts
		createDummyData();
	}

	private void createDummyData() {
		// Create test users
		createTestUser("john.doe@example.com", "John Doe", "password123");
		createTestUser("jane.smith@example.com", "Jane Smith", "password123");
		createTestUser("mike.wilson@example.com", "Mike Wilson", "password123");
		createTestUser("sarah.jones@example.com", "Sarah Jones", "password123");

		// Create test posts if none exist
		if (postRepository.count() == 0) {
			createTestPost("admin@horrorcore.com", "Welcome to Eva Social! 🎉 This is our new social media platform. #welcome #evasocial #launch");
			createTestPost("john.doe@example.com", "Just had an amazing coffee this morning! ☕ Nothing beats a good start to the day. #coffee #morning #goodvibes");
			createTestPost("jane.smith@example.com", "Working on some exciting new projects today! 💼 Can't wait to share what we're building. #work #projects #exciting #productivity");
			createTestPost("mike.wilson@example.com", "Beautiful sunset today 🌅 Nature never fails to amaze me! #sunset #nature #photography #beautiful");
			createTestPost("sarah.jones@example.com", "Learning React and Spring Boot! 📚 The combination is really powerful for full-stack development. #learning #react #springboot #development #coding");
			createTestPost("john.doe@example.com", "Weekend plans: hiking and reading 📖🥾 Perfect way to recharge! #weekend #hiking #reading #recharge");
			createTestPost("jane.smith@example.com", "Grateful for all the support from friends and family 💖 #grateful #family #friends #love");
			createTestPost("admin@horrorcore.com", "Just deployed some new features! 🚀 Check out the improved post creation and hashtag system. #deployment #features #development #innovation");
			
			System.out.println("Dummy test data created successfully!");
		}
	}

	private void createTestUser(String email, String displayName, String password) {
		if (!userCredentialRepository.existsById(email)) {
			UserCredential user = UserCredential.builder()
					.email(email)
					.password(passwordEncoder.encode(password))
					.role("USER")
					.build();
			userCredentialRepository.save(user);
			
			// Create profile and then update display name
			userProfileService.createProfile(user);
			
			// Update the profile with custom display name
			var updateRequest = new com.horrorcore.eva_social.dto.request.UpdateProfileRequest();
			updateRequest.setDisplayName(displayName);
			userProfileService.updateProfile(email, updateRequest);
		}
	}

	private void createTestPost(String authorEmail, String content) {
		UserCredential author = userCredentialRepository.findById(authorEmail)
				.orElse(null);
		
		if (author != null) {
			Post post = Post.builder()
					.status(content)
					.author(author)
					.isPublic(true)
					.isDraft(false)
					.viewCount(0L)
					.createdAt(LocalDateTime.now().minusDays((long) (Math.random() * 30))) // Random dates within last 30 days
					.updatedAt(LocalDateTime.now())
					.build();
			postRepository.save(post);
		}
	}
}
