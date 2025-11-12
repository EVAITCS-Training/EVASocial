package com.horrorcore;

import com.horrorcore.controllers.PostController;

public class SocialMediaApp {
    public static void main(String[] args) {
        PostController postController = new PostController();
        postController.postStatus();
    }
}
