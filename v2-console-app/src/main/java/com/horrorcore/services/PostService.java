package com.horrorcore.services;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.PostNewPostRequest;
import com.horrorcore.models.Post;
import com.horrorcore.repositories.InMemoryPostRepository;
import com.horrorcore.repositories.PostRepository;

import java.util.Arrays;

public class PostService {
    private PostRepository postRepository = new InMemoryPostRepository();

    public PostInformation createPost(PostNewPostRequest request) {
        Post post = new Post(request.status(), request.username());
        postRepository.save(post);
        return new PostInformation(
                post.getId(),
                post.getStatus(),
                post.getUsername(),
                post.getLikes(),
                post.getDislikes(),
                Arrays.asList(post.getComments())
        );
    }
}
