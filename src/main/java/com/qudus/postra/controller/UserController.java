package com.qudus.postra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qudus.postra.dtos.LoginRequest;
import com.qudus.postra.dtos.RegisterRequest;
import com.qudus.postra.model.Users;
import com.qudus.postra.service.UserService;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    // @PostMapping("/login")
    // public ResponseEntity<Object> login(@RequestBody LoginRequest request) {

    //     System.out.println("***Controller***");

    //     if (request.getUsernameOrEmail() == null || request.getUsernameOrEmail().isBlank()
    //             || request.getPassword() == null || request.getPassword().isBlank()) {
    //         return ResponseEntity.badRequest().body("Username/email and password are required");
    //     }

    //     Optional<Users> optionalUser = userService.findUserByEmailOrUsername(request.getUsernameOrEmail());

    //     if (optionalUser.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/email or password");
    //     }

    //     return ResponseEntity.ok("Login successful");
    // }
}
