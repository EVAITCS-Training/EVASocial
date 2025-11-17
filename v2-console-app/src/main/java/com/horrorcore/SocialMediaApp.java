package com.horrorcore;

import com.horrorcore.controllers.PostController;
import com.horrorcore.repositories.InMemoryPostRepository;
import com.horrorcore.repositories.PostRepository;
import com.horrorcore.services.PostService;
import com.horrorcore.ui.View;
import com.horrorcore.utils.DependencyContainer;

import java.util.Scanner;

public class SocialMediaApp {
    public static void main(String[] args) {
        //static, final, transient, volatile
        DependencyContainer dependencyContainer = new DependencyContainer();

        dependencyContainer.setService("repo", new InMemoryPostRepository());
        dependencyContainer.setService("scanner", new Scanner(System.in));
        dependencyContainer.setService("service", new PostService(
                (PostRepository) dependencyContainer.getService("repo")
        ));
        dependencyContainer.setService("controller", new PostController(
                (PostService) dependencyContainer.getService("service"),
                (Scanner) dependencyContainer.getService("scanner")));
        dependencyContainer.setService("view", new View(
                (PostController) dependencyContainer.getService("controller"),
                (Scanner) dependencyContainer.getService("scanner")));
        ((View) dependencyContainer.getService("view")).start();
    }
}
