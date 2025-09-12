package com.qudus.postra.dtos;

import com.qudus.postra.model.Posts;

public class PostsMapper {
    public static PostDto1 toDto(Posts posts) {
        return new PostDto1(
                posts.getId(),
                posts.getTitle(),
                posts.getSubTitle(),
                posts.getContent(),
                posts.getHeaderImage(),
                posts.getAuthor().getUserName(),
                posts.getAuthor().getProfilePic());
    }
}
