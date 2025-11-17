package com.horrorcore.ui;

import com.horrorcore.controllers.PostController;

import java.util.Scanner;

public class View {
    private PostController postController;
    private Scanner scanner;

    public View(PostController postController, Scanner scanner){
        this.postController = postController;
        this.scanner = scanner;
    }


    public void start() {
        while (true) {
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
                case 10:
                    System.out.println("Goodbye");
                    scanner.close();
                    System.exit(1);
                default:
                    System.out.println("Please enter a choice between 1-10");
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
                | 10. Exit                  |
                +---------------------------+
                """;
        System.out.println(menu);
    }
}
