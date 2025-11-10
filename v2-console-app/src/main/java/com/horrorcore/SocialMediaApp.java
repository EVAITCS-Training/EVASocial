package com.horrorcore;

import com.horrorcore.models.Post;

import java.time.LocalDateTime;
import java.util.Date;

public class SocialMediaApp {
    public static void main(String[] args) {

//        String h1 = "Hello";
//        String h2 = ", world!";
//        String h3 = h1 + h2;
//        String h4 = "hello";
//        String h5 = new String("Hello");
//        System.out.println(System.identityHashCode(h1));
//        System.out.println(h2.hashCode());
//        System.out.println(h3.hashCode());
//        System.out.println(h4.hashCode());
//        System.out.println(System.identityHashCode(h5));
//        System.out.println(System.identityHashCode(h5.intern()));
//        System.out.println(new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)));
//        System.out.println(System.currentTimeMillis());
//        System.out.println(LocalDateTime.now());
//        System.out.println(LocalDateTime.now().plusMonths(12));
        System.out.println(Post.getIdCounter());
        Post post = new Post("Test Post", "testuser001");
        Post post2 = new Post("Test Post", "testuser002");
        System.out.println(post.getStatus());
        System.out.println(post.getUsername());
        System.out.println(post.getCreatedAt());
        System.out.println(post.isDraft());
        System.out.println(post.getId());
        System.out.println(post2.getId());
        System.out.println(post.getIdCounter());
        post.setIdCounter(10);
        System.out.println(Post.getIdCounter());
        System.out.println(post2.getIdCounter());
        System.out.println(post.toString());
        System.out.println('☺');

    }
}
