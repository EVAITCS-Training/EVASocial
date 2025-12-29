package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.exceptions.PostNotFoundException;
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

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void updateLikes(long id) {
        if(!postRepository.existsById(id)) throw new PostNotFoundException("Posts with the id of " + id + " does not exists!");
        Post post = postRepository.findById(id).get();
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Override
    public void updateDislikes(long id) {
        if(!postRepository.existsById(id)) throw new PostNotFoundException("Posts with the id of " + id + " does not exists!");
        Post post = postRepository.findById(id).get();
        post.setDislikes(post.getDislikes() + 1);
        postRepository.save(post);
    }
}
