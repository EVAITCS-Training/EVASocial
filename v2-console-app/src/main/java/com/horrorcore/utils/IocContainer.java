package com.horrorcore.utils;

import com.horrorcore.controllers.PostController;
import com.horrorcore.repositories.JDBCPostRepository;
import com.horrorcore.repositories.PostRepository;
import com.horrorcore.services.PostService;
import com.horrorcore.ui.View;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class IocContainer {

    @Bean
    public PostRepository postRepository() {
        return new JDBCPostRepository();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public PostService postService() {
        return new PostService(postRepository());
    }

    @Bean
    public PostController postController() {
        return new PostController(postService(), scanner());
    }

    @Bean
    public View view() {
        return new View(postController(), scanner());
    }
}
