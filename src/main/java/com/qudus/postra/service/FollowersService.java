package com.qudus.postra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qudus.postra.dtos.FollowerDto;
import com.qudus.postra.model.FollowerId;
import com.qudus.postra.model.Followers;
import com.qudus.postra.model.UserProfile;
import com.qudus.postra.model.Users;
import com.qudus.postra.repository.FollowerRepo;
import com.qudus.postra.repository.UserRepo;

@Service
public class FollowersService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerRepo followersRepo;

    public boolean follow(String followerUsername, String followingUsername) {
        var extractedUsername = userRepo.findUserByEmail(followerUsername).get().getUserProfile().getUserName();
        // System.out.println(extractedUsername);
        try {
            Users follower = userRepo.findUserByUserProfile_UserName(extractedUsername)
                    .orElseThrow(() -> new RuntimeException("Follower user not found"));

            Users following = userRepo.findUserByUserProfile_UserName(followingUsername)
                    .orElseThrow(() -> new RuntimeException("User to follow not found"));

            // Check if the user is trying to follow themselves
            if (follower.getId().equals(following.getId())) {
                throw new RuntimeException("You cannot follow yourself");
            }

            // Check if already following
            boolean alreadyFollowing = followersRepo.existsByFollowerAndFollowing(follower, following);
            if (alreadyFollowing) {
                return false; // already following
            }

            // Create new follow relationship
            Followers follow = new Followers();
            follow.setFollower(follower);
            follow.setFollowing(following);
            follow.setId(new FollowerId(follower.getId(), following.getId()));

            followersRepo.save(follow);
            return true;

        } catch (Exception e) {
            System.out.println("Follow failed: " + e.getMessage());
            return false;
        }
    }

    public boolean unfollow(String followerUsername, String followingUsername) {
        var extractedUsername = userRepo.findUserByEmail(followerUsername).get().getUserProfile().getUserName();
        System.out.println(extractedUsername);

        try {
            Users follower = userRepo.findUserByUserProfile_UserName(extractedUsername)
                    .orElseThrow(() -> new RuntimeException("Follower user not found"));

            Users following = userRepo.findUserByUserProfile_UserName(followingUsername)
                    .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

            // Check if relationship exists
            FollowerId id = new FollowerId(follower.getId(), following.getId());
            boolean exists = followersRepo.existsById(id);
            if (!exists) {
                System.out.println("User is not following");
                return false;
            }

            // Delete the follow relationship
            followersRepo.deleteById(id);
            System.out.println(followerUsername + " unfollowed " + followingUsername);
            return true;

        } catch (Exception e) {
            System.out.println("Unfollow failed: " + e.getMessage());
            return false;
        }
    }

    public List<FollowerDto> getFollowing(String username) {
        Users user = userRepo.findUserByUserProfile_UserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Followers> following = followersRepo.findByFollower(user);

        return following.stream().map(f -> {
            UserProfile profile = f.getFollowing().getUserProfile();

            FollowerDto dto = new FollowerDto();
            dto.setUsername(profile.getUserName());
            dto.setFullName(profile.getFullName());
            dto.setProfileImg(profile.getProfilePic());

            return dto;
        }).toList();
    }

    public List<FollowerDto> getFollowers(String username) {
        Users user = userRepo.findUserByUserProfile_UserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Followers> followers = followersRepo.findByFollowing(user);

        return followers.stream().map(f -> {
            UserProfile profile = f.getFollower().getUserProfile();

            FollowerDto dto = new FollowerDto();
            dto.setUsername(profile.getUserName());
            dto.setFullName(profile.getFullName());
            dto.setProfileImg(profile.getProfilePic());

            return dto;
        }).toList();
    }

}
