package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Modify
// /api/[reasource_name]/
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private PostService postService;
    //Modify
    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<Post>> getPostsRequest() {
        return ResponseEntity.ok(postService.getAllPosts());
    }
    //Modify
    @PostMapping(value = {"/", ""})
    public ResponseEntity<Post> postNewStatus(@RequestBody @Valid Post post) {
        return ResponseEntity.created(null).body(postService.createPost(post));
    }
    //modify
    @PostMapping("/posts/update/likes/{id}")
    public String updateLikes(@PathVariable long id) {
        postService.updateLikes(id);
        return "redirect:/posts";
    }
    //modify
    @PostMapping("/posts/update/dislikes/{id}")
    public String updateDislikes(@PathVariable long id) {
        postService.updateDislikes(id);
        return "redirect:/posts";
    }
}
