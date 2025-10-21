package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.postra.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{postSlug}")
    public ResponseEntity<Object> like(@PathVariable String postSlug) {
        System.out.println("LikeController: Received request to toggle like for postSlug: " + postSlug);
        return ResponseEntity.ok(likeService.toggleLike(postSlug));
    }

    @GetMapping("/{postSlug}/count")
    public ResponseEntity<Long> getLikeCounts(@PathVariable String postSlug) {
        System.out.println("LikeController: Received request to get like count for postSlug: " + postSlug);
        return ResponseEntity.ok(likeService.getLikeCount(postSlug));
    }

    @GetMapping("/{postSlug}/is-liked")
    public ResponseEntity<Object> isLiked(@PathVariable String postSlug) {
        System.out.println("LikeController: Received request to check if liked for postSlug: " + postSlug);
        return ResponseEntity.ok(likeService.isLiked(postSlug));
    }

}
