package com.horrorcore.ui;

import com.horrorcore.controllers.PostController;

import java.util.Scanner;

/**
 * Lightweight console-based UI that drives the application. The View presents
 * a simple menu, reads choices from the shared Scanner and delegates actions
 * to the controller.
 */
public class View {
    private PostController postController;
    private Scanner scanner;

    public View(PostController postController, Scanner scanner){
        this.postController = postController;
        this.scanner = scanner;
    }


    public void start() {
        while (true) {
            try {
                mainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice) {
                    case 1:
                        postController.postStatus();
                        break;
                    case 2:
                        postController.viewAllPosts();
                        break;
                    case 3:
                        postController.changeDraftStatus();
                        break;
                    case 4:
                        postController.deletePostPrompt();
                        break;
                    case 10:
                        System.out.println("Goodbye");
                        scanner.close();
                        System.exit(1);
                    default:
                        System.out.println("Please enter a choice between 1-10");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void mainMenu() {
        String menu = """
                +---------------------------+
                |       Social Media        |
                +---------------------------+
                | 1. Post a status update   |
                | 2. View all statuses      |
                | 3. Change Draft Status    |
                | 4. Delete Post            |
                | 10. Exit                  |
                +---------------------------+
                throw new RuntimeException();
                """;
        System.out.println(menu);
    }
}
