package com.qudus.postra.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import com.qudus.postra.dtos.LoginRequest;
import com.qudus.postra.dtos.RegisterRequest;
import com.qudus.postra.dtos.UsersDto;
import com.qudus.postra.model.ApiResponse;
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
    public ResponseEntity<ApiResponse<UsersDto>> login(
            @RequestBody LoginRequest request,
            @RequestHeader(name = "X-Client-Type", required = false) String clientType,
            HttpServletRequest httpServletRequest) {

        System.out.println("***Controller***");

        try {
            if (clientType == null || clientType.isBlank()) {
                throw new Exception("Client Type is required");
            }
    
            if (request.getUsernameOrEmail() == null || request.getUsernameOrEmail().isBlank()
                    || request.getPassword() == null || request.getPassword().isBlank()) {
                throw new Exception("Username/email and password are required");
            }
    
            String jwt = userService.verify(request.getUsernameOrEmail(), request.getPassword());
    
            if ("fail".equalsIgnoreCase(jwt)) {
                throw new Exception("Unable to verify user");
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
                ApiResponse<UsersDto> response = new ApiResponse<UsersDto>("success", "Login successful", userService.getUser(request.getUsernameOrEmail()), null);
    
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
            }
            ApiResponse<UsersDto> response = new ApiResponse<UsersDto>("success", jwt, userService.getUser(request.getUsernameOrEmail()), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            ApiResponse<UsersDto> response = new ApiResponse<UsersDto>("error", "Error logging you in", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<ApiResponse<UsersDto>> getUserProfile(@PathVariable String username) {
        System.out.println("Fetching profile for user: " + username);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (auth == null) {
    System.out.println("Authentication is null");
} else {
    System.out.println("Authentication type: " + auth.getClass());
    System.out.println("Authenticated: " + auth.isAuthenticated());
    System.out.println("Principal: " + auth.getPrincipal());
    System.out.println("Authorities: " + auth.getAuthorities());
}

        try {
            UsersDto user = userService.getUser(username);
            if (user == null) {
                throw new Exception("User now found");
            }
            ApiResponse<UsersDto> response = new ApiResponse<UsersDto>("success", "Success getting user", user, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<UsersDto> response = new ApiResponse<UsersDto>("error", "Error getting user", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
