package com.qudus.postra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qudus.postra.dtos.FollowerDto;
import com.qudus.postra.model.FollowId;
import com.qudus.postra.model.Follow;
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

    public void toggleFollow(String followerUsername, String followingUsername) {
        Users follower = userRepo.findUserByUserProfile_UserName(followerUsername)
                .orElseThrow(() -> new RuntimeException("Follower user not found"));

        Users following = userRepo.findUserByUserProfile_UserName(followingUsername)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (follower.getId().equals(following.getId())) {
            throw new RuntimeException("You cannot follow yourself");
        }

        boolean alreadyFollowing = followersRepo.existsByFollowerAndFollowing(follower, following);

        if (alreadyFollowing) {
            followersRepo.deleteById(new FollowId(follower.getId(), following.getId()));
        } else {
            followersRepo.save(new Follow(follower, following));
        }
    }

    public List<FollowerDto> getFollowing(String username) {
        Users user = userRepo.findUserByUserProfile_UserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Follow> following = followersRepo.findByFollower(user);

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

        List<Follow> followers = followersRepo.findByFollowing(user);

        return followers.stream().map(f -> {
            UserProfile profile = f.getFollower().getUserProfile();

            FollowerDto dto = new FollowerDto();
            dto.setUsername(profile.getUserName());
            dto.setFullName(profile.getFullName());
            dto.setProfileImg(profile.getProfilePic());

            return dto;
        }).toList();
    }

    public boolean isFollowing(String followingUsername, String followerUsername) {
        Users follower = userRepo.findUserByUserProfile_UserName(followerUsername)
                .orElseThrow(() -> new RuntimeException("Follower user not found"));

        Users following = userRepo.findUserByUserProfile_UserName(followingUsername)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        return followersRepo.existsByFollowerAndFollowing(follower, following);
    }

}
