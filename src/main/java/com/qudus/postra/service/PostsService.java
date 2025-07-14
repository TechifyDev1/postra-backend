package com.qudus.postra.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}
