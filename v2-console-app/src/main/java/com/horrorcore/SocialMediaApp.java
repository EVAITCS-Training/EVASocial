package com.horrorcore;

import com.horrorcore.controllers.PostController;
import com.horrorcore.repositories.InMemoryPostRepository;
import com.horrorcore.repositories.PostRepository;
import com.horrorcore.services.PostService;
import com.horrorcore.ui.View;
import com.horrorcore.utils.DependencyContainer;

import java.util.Scanner;

/**
 * Application entry point for the simple console social media app.
 * <p>
 * This main class performs a very small manual dependency wiring using
 * {@link DependencyContainer} and then starts the {@link View} which presents
 * a console-based UI to the user.
 * </p>
 * Responsibilities:
 * - Create and register a repository implementation
 * - Create and register the service, controller and view
 * - Start the console view loop
 */
public class SocialMediaApp {
    public static void main(String[] args) {
        //static, final, transient, volatile
        DependencyContainer dependencyContainer = DependencyContainer.getInstance();

        // Register a simple in-memory repository for storing posts
        dependencyContainer.setService("repo", new InMemoryPostRepository());
        // Register a single Scanner instance shared across the app for console IO
        dependencyContainer.setService("scanner", new Scanner(System.in));
        // Register service with repository dependency resolved from the container
        dependencyContainer.setService("service", new PostService(
                (PostRepository) dependencyContainer.getService("repo")
        ));
        // Register controller with service + scanner
        dependencyContainer.setService("controller", new PostController(
                (PostService) dependencyContainer.getService("service"),
                (Scanner) dependencyContainer.getService("scanner")));
        // Register view with controller + scanner and start the app
        dependencyContainer.setService("view", new View(
                (PostController) dependencyContainer.getService("controller"),
                (Scanner) dependencyContainer.getService("scanner")));
        ((View) dependencyContainer.getService("view")).start();
    }
}
