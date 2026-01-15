package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Test
    void savePostSuccessTest() {
        Post post = Post.builder()
                .status("Test Status")
                .isPublic(true)
                .isDraft(false)
                .build();

        Post savedPost = Post.builder()
                .id(1)
                .status("Test Status")
                .isPublic(true)
                .isDraft(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postRepository.save(post);

        assertNotNull(result);
        assertEquals("Test Status", result.getStatus());
        assertEquals(1, result.getId());
        assertTrue(result.isPublic());
        assertFalse(result.isDraft());
    }

    @Test
    void findPostByIdSuccessTest() {
        Post post = Post.builder()
                .id(1)
                .status("Test Status")
                .isPublic(true)
                .isDraft(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Optional<Post> result = postRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Status", result.get().getStatus());
    }
}
