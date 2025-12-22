package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PostController {
    private PostService postService;

    @GetMapping(value = {"/posts/", "/posts"})
    public String getPostsIndex(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts-index";
    }
}
