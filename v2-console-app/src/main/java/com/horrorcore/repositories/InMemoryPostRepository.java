package com.horrorcore.repositories;

import com.horrorcore.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A very small in-memory repository for Post objects. This implementation is
 * not thread-safe and intended for demos and tests only. Data is lost when
 * the JVM exits.
 */
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
        //Java 8|| Functional Programming
        return posts.stream()
                .filter(post -> post.getUsername().equalsIgnoreCase(username))
                .toList();
    }

    @Override
    public Optional<Post> findById(long id) {
        return posts.stream().filter(post -> post.getId() == id)
                .findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return posts.removeIf(post -> post.getId() == id);
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }
}
