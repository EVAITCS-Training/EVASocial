package com.horrorcore.repositories;

import com.horrorcore.models.Post;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPostRepository implements PostRepository {
    List<Post> posts = new ArrayList<>();

    @Override
    public Post save(Post post) {
        boolean result = posts.add(post);
        if(result){
            return post;
        }
        return null;
    }

    @Override
    public List<Post> findAll() {
        return posts;
    }

    @Override
    public List<Post> findAllByUsername(String username) {
//        List<Post> postsWithUsername = new ArrayList<>(); // Object Orient Way
//        for(Post post : posts) {
//            if(post.getUsername().equalsIgnoreCase(username)) {
//                postsWithUsername.add(post);
//            }
//        }
//        return postsWithUsername;
        //Java 8|| Functional Programming
        return posts.stream()
                .filter(post -> post.getUsername().equalsIgnoreCase(username))
                .toList();
    }
}
