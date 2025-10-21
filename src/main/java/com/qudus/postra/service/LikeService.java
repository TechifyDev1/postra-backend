package com.qudus.postra.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qudus.postra.model.Like;
import com.qudus.postra.model.Posts;
import com.qudus.postra.model.Users;
import com.qudus.postra.repository.LikeRepo;
import com.qudus.postra.repository.PostRepo;
import com.qudus.postra.repository.UserRepo;

@Service
public class LikeService {
    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public Map<String, Object> toggleLike(String postSlug) {
        Optional<Posts> post = postRepo.findBySlug(postSlug);
        if (post.isEmpty()) {
            return Map.of("message", "Post not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Users> user = userRepo.findUserByEmail(email);
        if (user.isEmpty()) {
            return Map.of("message", "No logged-in user found");
        }

        boolean isLiked = likeRepo.existsByUserAndPost(user.get(), post.get());

        if (isLiked) {
            likeRepo.deleteByUserAndPost(user.get(), post.get());
            long totalLikes = likeRepo.countByPost(post.get());
            System.out.println(Map.of("message", "Unliked", "totalLikes", totalLikes));
            return Map.of(
                    "message", "Unliked",
                    "totalLikes", totalLikes);
        }

        Like like = new Like();
        like.setPost(post.get());
        like.setUser(user.get());
        like.setCreatedAt(LocalDateTime.now());
        likeRepo.save(like);

        long totalLikes = likeRepo.countByPost(post.get());
        System.out.println(Map.of("message", "Liked", "totalLikes", totalLikes));
        return Map.of(
                "message", "Liked",
                "totalLikes", totalLikes);
    }

    public long getLikeCount(String postSlug) {
        Optional<Posts> post = postRepo.findBySlug(postSlug);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        return likeRepo.countByPost(post.get());
    }

    public boolean isLiked(String postSlug) {
        Optional<Posts> post = postRepo.findBySlug(postSlug);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Users> user = userRepo.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("No logged-in user found");
        }

        return likeRepo.existsByUserAndPost(user.get(), post.get());
    }
}
