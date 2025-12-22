package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("user")
@AllArgsConstructor
public class UserPostServiceImpl implements PostService{

    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
