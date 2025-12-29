package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class PostController {
    private PostService postService;

    @GetMapping(value = {"/posts/", "/posts"})
    public String getPostsIndex(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts-index";
    }

    @GetMapping(value = {"/posts/create", "/posts/create/"})
    public String getCreatePostPage(Model model) {
        model.addAttribute("newPost", new Post());
        return "post-form";
    }

    @PostMapping(value = {"/posts/create", "/posts/create/"})
    public String postNewStatus(@ModelAttribute("newPost") @Valid Post post, Errors errors) {
        if(errors.hasErrors()) return "post-form";
        postService.createPost(post);
        return "redirect:/posts";
    }

    @PostMapping("/posts/update/likes/{id}")
    public String updateLikes(@PathVariable long id) {
        postService.updateLikes(id);
        return "redirect:/posts";
    }

    @PostMapping("/posts/update/dislikes/{id}")
    public String updateDislikes(@PathVariable long id) {
        postService.updateDislikes(id);
        return "redirect:/posts";
    }
}
