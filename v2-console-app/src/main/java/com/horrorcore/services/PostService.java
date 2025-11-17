package com.horrorcore.services;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.PostNewPostRequest;
import com.horrorcore.models.Post;
import com.horrorcore.repositories.InMemoryPostRepository;
import com.horrorcore.repositories.PostRepository;
import com.horrorcore.utils.PostMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostInformation createPost(PostNewPostRequest request) {
        Post post = new Post(request.status(), request.username());
        postRepository.save(post);
        return PostMapper.toDto(post);
    }

    public List<PostInformation> getAllPosts() {
//        List<PostInformation> postInformations = new ArrayList<>();
//        for (Post post : postRepository.findAll()) {
//            postInformations.add(PostMapper.toDto(post));
//        }
//        return postInformations;
        return postRepository.findAll().stream()
//                .peek(p -> System.out.println("Before Filter" + p.toString()))
                .filter(Predicate.not(Post::isDraft))
//                .peek(p -> System.out.println("After Filter" + p.toString()))
                .map(PostMapper::toDto)
                .toList();
    }

    public boolean changeDraftStatus(long id) {
        Post post = postRepository.findAll().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if(post == null) return false;
        post.setDraft(false);
        return true;
    }

    public List<PostInformation> findAllByUsername(String username) {
        return postRepository.findAllByUsername(username).stream().map(PostMapper::toDto).toList();
    }
}
