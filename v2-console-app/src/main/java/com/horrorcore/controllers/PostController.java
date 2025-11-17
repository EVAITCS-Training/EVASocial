package com.horrorcore.controllers;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.PostNewPostRequest;
import com.horrorcore.services.PostService;

import java.util.Scanner;

/**
 * Controller layer responsible for handling console-driven user interactions
 * related to posts. The controller reads input from a shared {@link Scanner}
 * and delegates business logic to {@link PostService}.
 *
 * Responsibilities:
 * - Prompting the user for input
 * - Converting raw input into DTOs/requests
 * - Calling the service and displaying results
 */
public class PostController {
    private PostService postService;
    private Scanner scanner;

    public PostController(PostService postService, Scanner scanner) {
        this.postService = postService;
        this.scanner = scanner;
    }

    /**
     * Ask the user for a status and username, create a new post and show the
     * created post information. This action will create a draft post (see
     * {@link com.horrorcore.models.Post}) which can be finalized later.
     */
    public void postStatus() {
        System.out.println("Please input your status!");
        String status = scanner.nextLine();
        System.out.println("Please enter your username!");
        String username = scanner.nextLine();
        System.out.println("Creating Post.....");
        PostInformation postInformation = postService.createPost(new PostNewPostRequest(status, username));
        System.out.println("Post Created");
        System.out.println(postInformation);
    }

    /**
     * Retrieve and print all non-draft posts (the service filters drafts)
     */
    public void viewAllPosts() {
        System.out.println("Here is all of the available posts:");
        postService.getAllPosts().forEach(System.out::println);
    }

    /**
     * List posts for a username, prompt for an id, and mark the chosen post as
     * no longer a draft.
     */
    public void changeDraftStatus() {
        System.out.println("please enter your username");
        String username = scanner.nextLine();
        postService.findAllByUsername(username).forEach(System.out::println);
        System.out.println("Please enter an id to final a post");
        int choice = scanner.nextInt();
        postService.changeDraftStatus(choice);
    }
}
