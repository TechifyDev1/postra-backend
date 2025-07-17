package com.qudus.postra.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.qudus.postra.dtos.PostDto;
import com.qudus.postra.model.Posts;
import com.qudus.postra.model.UserProfile;
import com.qudus.postra.repository.PostRepo;
import com.qudus.postra.repository.UserProfileRepo;
import com.qudus.postra.utils.Slug;

@Service
public class PostsService {
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserProfileRepo profileRepo;

    public List<Posts> getAll() {
        return postRepo.findAll();
    }

    public PostDto create(String title, String subTitle, String content, String headerImage) {

        Slug slug = new Slug(title);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email.isBlank() || email.isEmpty()) {
            throw new RuntimeException("username not found");
        }
        Optional<UserProfile> userProfile = profileRepo.findByUser_Email(email);
        if (userProfile.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        String slugString = slug.generateUniqueSlug();

        Posts post = new Posts();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(userProfile.get());
        post.setSlug(slugString);
        post.setHeaderImage(headerImage);
        post.setSubTitle(subTitle);
        Posts savedPost = postRepo.save(post);
        return new PostDto(title, subTitle, headerImage, userProfile.get().getFullName(), savedPost.getId(), content,
                slugString,
                userProfile.get().getUserName());
    }

    public boolean delete(String slug) {
        try {
            // Get logged-in user's email
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            if (email == null || email.isBlank()) {
                return false;
            }

            // Find the post by slug
            var postOptional = postRepo.findBySlug(slug);
            if (postOptional.isEmpty()) {
                return false;
            }

            var post = postOptional.get();
            var authorUsername = post.getAuthor().getUserName(); // Or getUserProfile().getUserName()

            // Find the profile of the logged-in user
            var profileOptional = profileRepo.findByUser_Email(email);
            if (profileOptional.isEmpty()) {
                return false;
            }

            var currentUsername = profileOptional.get().getUserName();

            // Compare usernames
            if (currentUsername.equals(authorUsername)) {
                postRepo.delete(post);
                return true;
            }

            return false;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public PostDto update(String slug, PostDto postDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Posts post = postRepo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        UserProfile profile = profileRepo.findByUser_Email(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

        if (!profile.getUserName().equals(post.getAuthor().getUserName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You’re not allowed to update this post");
        }

        if (postDto.getTitle() != null) {
            post.setTitle(postDto.getTitle());
            Slug slug2 = new Slug(postDto.getTitle());
            post.setSlug(slug2.generateUniqueSlug());
        }
        if (postDto.getSubTitle() != null) {
            post.setSubTitle(postDto.getSubTitle());
        }
        if (postDto.getContent() != null) {
            post.setContent(postDto.getContent());
        }
        if (postDto.getPostBanner() != null) {
            post.setHeaderImage(postDto.getPostBanner());
        }

        Posts updated = postRepo.save(post);

        return new PostDto(
                updated.getTitle(),
                updated.getSubTitle(),
                updated.getHeaderImage(),
                updated.getAuthor().getFullName(),
                updated.getId(),
                updated.getContent(),
                updated.getSlug(),
                updated.getAuthor().getUserName());
    }

    

}
