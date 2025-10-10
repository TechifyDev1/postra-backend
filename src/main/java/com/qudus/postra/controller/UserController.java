package com.qudus.postra.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import com.qudus.postra.dtos.LoginRequest;
import com.qudus.postra.dtos.RegisterRequest;
import com.qudus.postra.dtos.UsersDto;
import com.qudus.postra.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.getFullName() == null || registerRequest.getFullName().isBlank()
                || registerRequest.getEmail() == null || registerRequest.getEmail().isBlank()
                || registerRequest.getPassword() == null || registerRequest.getPassword().isBlank()
                || registerRequest.getUsername() == null || registerRequest.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body("Username, password, full name and email are required");
        }

        boolean success = userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getFullName());

        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists");
        }

        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            @RequestHeader(name = "X-Client-Type", required = false) String clientType,
            HttpServletRequest httpServletRequest) {

        System.out.println("***Controller***");

        if (clientType == null || clientType.isBlank()) {
            return ResponseEntity.badRequest().body("Client type is required");
        }

        if (request.getUsernameOrEmail() == null || request.getUsernameOrEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Username/email and password are required");
        }

        String jwt = userService.verify(request.getUsernameOrEmail(), request.getPassword());

        if ("fail".equalsIgnoreCase(jwt)) {
            return ResponseEntity.badRequest().body("Unable to verify user");
        }

        // boolean isLocal = "localhost".equalsIgnoreCase(httpServletRequest.getServerName());

        if ("web".equalsIgnoreCase(clientType)) {
            ResponseCookie cookie = ResponseCookie.from("token", jwt)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("message", "Login successful", "status", "success"));
        }

        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        System.out.println("Fetching profile for user: " + username);
        UsersDto userDto = userService.getUser(username);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found", "status", "fail"));
        }
        return ResponseEntity.ok(userDto);
    }

}
