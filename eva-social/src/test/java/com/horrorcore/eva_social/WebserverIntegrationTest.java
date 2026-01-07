package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportTestcontainers
public class WebserverIntegrationTest {

    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("pass");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnWithPostCount() {
        ResponseEntity<List<Post>> responseEntity = restTemplate.exchange(
                "/api/posts/",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Post>>() {}
        );
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
        // Optionally, assert the count: assertThat(responseEntity.getBody().size()).isEqualTo(expectedCount);
    }
}