package com.horrorcore.eva_social;

import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import com.horrorcore.eva_social.services.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
public class WebserverIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userProfileRepository.deleteAll();
        userCredentialRepository.deleteAll();
        
        // Create a test user
        UserCredential testUser = UserCredential.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .role("USER")
                .build();
        userCredentialRepository.save(testUser);
        userProfileService.createProfile(testUser);
        
        // Create 5 dummy posts
        for (int i = 1; i <= 5; i++) {
            Post post = Post.builder()
                    .author(testUser)
                    .status("This is test post number " + i)
                    .isPublic(true)
                    .isDraft(false)
                    .build();
            postRepository.save(post);
        }
    }

    @Test
    void shouldReturnWithPostCount() {
        ResponseEntity<List<PostResponse>> responseEntity = restTemplate.exchange(
                "/api/v1/posts",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostResponse>>() {}
        );
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(5);
    }
}
