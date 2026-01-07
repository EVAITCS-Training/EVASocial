package com.horrorcore.eva_social;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.services.UserPostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UserPostServiceImpl postService;

    @Test
    void createPostsSuccessTest() {
        Post post = new Post();
        post.setStatus("Test Status");

        Post savedPost = new Post();
        savedPost.setId(1);
        savedPost.setStatus("Test Status");
        savedPost.setDislikes(0);
        savedPost.setLikes(0);
        savedPost.setCreatedAt(LocalDateTime.now());
        savedPost.setUpdatedAt(LocalDateTime.now());
        savedPost.setComments(new ArrayList<>());

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postService.createPost(post);

        assertEquals("Test Status", result.getStatus());
        assertEquals(0, result.getLikes());
        assertEquals(0, result.getDislikes());
    }
}