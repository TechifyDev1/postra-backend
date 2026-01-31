package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.postra.model.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> follow(@PathVariable String targetUsername) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(currentUserEmail);
            boolean followed = followersService.follow(currentUserEmail, targetUsername);
            if (followed) {
                ApiResponse<String> response = new ApiResponse<String>("success", "Successfully followed " + targetUsername, targetUsername, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            ApiResponse<String> response = new ApiResponse<String>("error", "Error following  " + targetUsername, targetUsername, "Error following user");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<String>("error", "Error following  " + targetUsername, targetUsername, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/unfollow/{targetUsername}")
    public ResponseEntity<ApiResponse<String>> unFollow(@PathVariable String targetUsername) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(currentUserEmail);
            boolean unfollowed = followersService.follow(currentUserEmail, targetUsername);
            if (unfollowed) {
                ApiResponse<String> response = new ApiResponse<String>("success", "Successfully unfollowed " + targetUsername, targetUsername, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            ApiResponse<String> response = new ApiResponse<String>("error", "Error unfollowing  " + targetUsername, targetUsername, "Error unfollowing " + targetUsername);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<String>("error", "Error unfollowing  " + targetUsername, targetUsername, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
