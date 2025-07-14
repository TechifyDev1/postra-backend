package com.qudus.postra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qudus.postra.model.UserProfile;
import com.qudus.postra.model.Users;
import com.qudus.postra.repository.UserProfileRepo;
import com.qudus.postra.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @Autowired
    private UserProfileRepo userProfileRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    public boolean createUser(String username, String password, String email, String fullName) {
        try {
            if (userProfileRepo.findUserByUserName(username).isPresent()
                    || userRepo.findUserByEmail(email).isPresent()) {
                return false;
            }

            Users user = new Users();
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            userRepo.save(user);

            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user);
            userProfile.setUserName(username);
            userProfile.setFullName(fullName);

            userProfileRepo.save(userProfile);

            return true;

        } catch (Exception e) {
            System.out.println("❌ Unable to create user: " + e.getMessage());
            return false;
        }
    }

    // public Optional<Users> findUserByEmailOrUsername(String emailOrUsername) {
    // System.out.println("***USerService***");
    // if (emailOrUsername.contains("@")) {
    // System.out.println("***USerService Email***");
    // return userRepo.findUserByEmail(emailOrUsername);
    // } else {
    // System.out.println("***USerService username***");
    // return userRepo.findUserByUserProfile_UserName(emailOrUsername);
    // }
    // }

    public String verify(String username, String password) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            if (authentication.isAuthenticated()) {
                // Find user by username or email
                Users user;
                if (username.contains("@")) {
                    user = userRepo.findUserByEmail(username).orElse(null);
                } else {
                    user = userRepo.findUserByUserProfile_UserName(username).orElse(null);
                }
                if (user == null)
                    return "fail";
                // Always use email as JWT subject
                return jwtService.generateToken(user.getEmail());
            } else {
                return "fail";
            }
        } catch (org.springframework.security.core.AuthenticationException e) {
            System.out.println("failed " + e.getMessage());
            return "fail";
        }
    }

}
