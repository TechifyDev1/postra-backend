package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.postra.service.FollowersService;

@RestController
@RequestMapping
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    private ResponseEntity<Object> validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        return null;
    }

    @GetMapping("/api/users/{username}/followers")
    public ResponseEntity<Object> getFollowers(@PathVariable String username) {
        ResponseEntity<Object> validation = validateUsername(username);
        if (validation != null) {
            return validation;
        }
        return ResponseEntity.ok().body(followersService.getFollowers(username));
    }

    @GetMapping("/api/users/{username}/following")
    public ResponseEntity<Object> getFollowings(@PathVariable String username) {
        ResponseEntity<Object> validation = validateUsername(username);
        if (validation != null) {
            return validation;
        }
        return ResponseEntity.ok().body(followersService.getFollowing(username));
    }

    @PostMapping("/api/follow/{targetUsername}")
    public ResponseEntity<Object> follow(@PathVariable String targetUsername) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(currentUserEmail);
        boolean followed = followersService.follow(currentUserEmail, targetUsername);
        if (followed) {
            return ResponseEntity.ok("Successfully followed " + targetUsername);
        }
        return ResponseEntity.badRequest().body("Follow failed");
    }

    @PostMapping("/api/unfollow/{targetUsername}")
    public ResponseEntity<Object> unFollow(@PathVariable String targetUsername) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean followed = followersService.unfollow(currentUserEmail, targetUsername);
        if (followed) {
            return ResponseEntity.ok("Successfully Unfollowed " + targetUsername);
        }
        return ResponseEntity.badRequest().body("Unfollow failed");
    }

}
