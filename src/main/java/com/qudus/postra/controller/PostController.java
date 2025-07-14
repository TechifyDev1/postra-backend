package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.postra.dtos.PostDto;
import com.qudus.postra.service.PostsService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostsService postService;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@RequestBody PostDto post) {
        System.out.println("***Creating post***");
        if (post.getTitle() == null || post.getContent() == null || post.getTitle().isEmpty()
                || post.getContent().isEmpty() || post.getSubTitle() == null) {
            return ResponseEntity.badRequest().body("Title, content, subtitle and author must not be empty");
        }
        if (post.getTitle().length() < 5 || post.getContent().length() < 10) {
            return ResponseEntity.badRequest()
                    .body("Title must be at least 5 characters and content at least 10 characters long");
        }
        return ResponseEntity.ok()
                .body(postService.create(post.getTitle(), post.getSubTitle(), post.getContent(), post.getPostBanner()));
    }

    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<Object> deletePost(@PathVariable String slug) {
        if (slug.isEmpty() || slug.isBlank() || slug == null) {
            return ResponseEntity.badRequest().body("Slug must be provided");
        }
        boolean success = postService.delete(slug);
        if (success) {
            return ResponseEntity.ok().body("Deleted");
        }
        return ResponseEntity.badRequest().body("Something went wrong");
    }

    @PutMapping("/update/{slug}")
    public ResponseEntity<Object> updatePost(@PathVariable String slug, @RequestBody PostDto post) {
        if (post != null) {
            return ResponseEntity.ok().body(postService.update(slug, post.getContent(), post.getTitle(), post.getSubTitle(), post.getPostBanner()));
        }
        return ResponseEntity.badRequest().body("Something went wrong");
    }
}
