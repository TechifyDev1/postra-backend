package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse<Object>> toggleFollow(
            @PathVariable String targetUsername,
            @AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("error", "User not authenticated", null, null));
        }

        try {
            followersService.toggleFollow(user.getUsername(), targetUsername);
            return ResponseEntity.ok(new ApiResponse<>("success", "Follow/Unfollow succeeded", null, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "Something went wrong", null, e));
        }
    }

    @GetMapping("/api/follow/is-following/{targetUsername}")
    public ResponseEntity<ApiResponse<Boolean>> isFollowing(
            @PathVariable String targetUsername,
            @AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("error", "User not authenticated", null, null));
        }

        try {
            boolean following = followersService.isFollowing(targetUsername, user.getUsername());
            return ResponseEntity.ok(new ApiResponse<>("success",
                    following ? "following" : "not following",
                    following,
                    null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "Something went wrong", null, e));
        }
    }
}
