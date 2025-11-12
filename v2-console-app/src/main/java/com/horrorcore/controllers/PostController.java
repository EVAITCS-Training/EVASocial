package com.horrorcore.controllers;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.PostNewPostRequest;
import com.horrorcore.services.PostService;

import java.util.Scanner;

public class PostController {
    private PostService postService = new PostService();
    private Scanner scanner = new Scanner(System.in);

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
}
