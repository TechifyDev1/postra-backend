package com.qudus.postra.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.qudus.postra.dtos.CommentDto;
import com.qudus.postra.model.Comment;
import com.qudus.postra.model.Posts;
import com.qudus.postra.model.UserProfile;
import com.qudus.postra.repository.CommentRepo;
import com.qudus.postra.repository.PostRepo;
import com.qudus.postra.repository.UserProfileRepo;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserProfileRepo profileRepo;

    @Autowired
    private PostRepo postRepo;

    public CommentDto addComment(String content, String postSlug) {
        Comment comment = new Comment();
        comment.setContent(content);
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();
        Optional<UserProfile> userProfile = profileRepo.findByUser_Email(email);
        Optional<Posts> post = postRepo.findBySlug(postSlug);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        comment.setPost(post.get());

        if (userProfile.isPresent()) {
            comment.setAuthorUsername(userProfile.get().getUserName());
            commentDto.setAuthorUsername(comment.getAuthorUsername());
        } else {
            throw new RuntimeException("User profile not found");
        }

        System.out.println("Adding comment by " + comment.getAuthorUsername() + " on post " + postSlug);

        commentRepo.save(comment);
        return commentDto;
    }

    public List<CommentDto> getComments(String slug) {
        Optional<Posts> post = postRepo.findBySlug(slug);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        List<Comment> comments = commentRepo.findByPost(post.get());
        return comments.stream().map(comment -> {
            CommentDto dto = new CommentDto();
            dto.setContent(comment.getContent());
            dto.setAuthorUsername(comment.getAuthorUsername());
            return dto;
        }).toList();
    }
}
