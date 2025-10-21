package com.qudus.postra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.postra.dtos.CommentDto;
import com.qudus.postra.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Comment Controller is working!");
    }

    @PostMapping("/add/{postSlug}")
    public ResponseEntity<?> addComment(@PathVariable String postSlug, @RequestBody CommentDto commentDto) {
        if (commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body("Content Blank");
        }
        CommentDto createdComment = commentService.addComment(commentDto.getContent(), postSlug);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getComments(@PathVariable String slug) {
        List<CommentDto> comments = commentService.getComments(slug);
        return ResponseEntity.ok(comments);
    }

}
