package com.horrorcore.controllers;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.PostNewPostRequest;
import com.horrorcore.services.PostService;

import java.util.Scanner;

public class PostController {
    private PostService postService;
    private Scanner scanner;

    public PostController(PostService postService, Scanner scanner) {
        this.postService = postService;
        this.scanner = scanner;
    }

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

    public void viewAllPosts() {
        System.out.println("Here is all of the available posts:");
        postService.getAllPosts().forEach(System.out::println);
    }

    public void changeDraftStatus() {
        System.out.println("please enter your username");
        String username = scanner.nextLine();
        postService.findAllByUsername(username).forEach(System.out::println);
        System.out.println("Please enter an id to final a post");
        int choice = scanner.nextInt();
        postService.changeDraftStatus(choice);
    }
}
