package com.qudus.postra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qudus.postra.dtos.CurrentUserDto;
import com.qudus.postra.dtos.UsersDto;
import com.qudus.postra.model.UserProfile;
import com.qudus.postra.model.Users;
import com.qudus.postra.repository.PostRepo;
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

    @Autowired
    private PostRepo postRepo;

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
                // use username as JWT subject
                return jwtService.generateToken(user.getUserProfile().getUserName());
            } else {
                return "fail";
            }
        } catch (AuthenticationException e) {
            System.out.println("failed " + e.getMessage());
            return "fail";
        }
    }

    public UsersDto getUser(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String lookupUsername = username;
        if ("me".equals(username)) {
            if (auth != null && auth.isAuthenticated()) {
                lookupUsername = auth.getName();
            } else {
                return null;
            }
        }

        Users user = userRepo.findUserByUserProfile_UserName(lookupUsername).orElse(null);

        if (user == null) {
            return null;
        }

        boolean isCurrentUser = auth != null && auth.isAuthenticated() && auth.getName().equals(lookupUsername);
        long userPostCount = postRepo.countByAuthorUserName(username);
        if (isCurrentUser) {
            CurrentUserDto currentUserDto = new CurrentUserDto();
            currentUserDto.setEmail(user.getEmail());
            currentUserDto.setFullName(user.getUserProfile().getFullName());
            currentUserDto.setUsername(user.getUserProfile().getUserName());
            currentUserDto.setNumOfFollowers(user.getFollowers().size());
            currentUserDto.setNumOfFollowing(user.getFollowing().size());
            currentUserDto.setProfilePictureUrl(user.getUserProfile().getProfilePic());
            currentUserDto.setBgImage(user.getUserProfile().getBgImage());
            currentUserDto.setCurrentUser(true);
            currentUserDto.setPostCount(userPostCount);
            currentUserDto.setBio(user.getUserProfile().getBio());
            return currentUserDto;
        } else {
            UsersDto userDto = new UsersDto();
            userDto.setFullName(user.getUserProfile().getFullName());
            userDto.setUsername(user.getUserProfile().getUserName());
            userDto.setNumOfFollowers(user.getFollowers().size());
            userDto.setNumOfFollowing(user.getFollowing().size());
            userDto.setProfilePictureUrl(user.getUserProfile().getProfilePic());
            userDto.setBgImage(user.getUserProfile().getBgImage());
            userDto.setPostCount(userPostCount);
            userDto.setBio(user.getUserProfile().getBio());
            return userDto;
        }
    }
    public boolean updateUserProfile(String username, com.qudus.postra.dtos.UpdateUserRequest request) {
        try {
            Users user = userRepo.findUserByUserProfile_UserName(username).orElse(null);
            if (user == null) {
                return false;
            }

            UserProfile profile = user.getUserProfile();
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                profile.setFullName(request.getFullName());
            }
            if (request.getBio() != null) {
                profile.setBio(request.getBio());
            }
            if (request.getProfilePic() != null) {
                profile.setProfilePic(request.getProfilePic());
            }
            if (request.getBgImage() != null) {
                profile.setBgImage(request.getBgImage());
            }

            userProfileRepo.save(profile);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Unable to update user profile: " + e.getMessage());
            return false;
        }
    }

}
