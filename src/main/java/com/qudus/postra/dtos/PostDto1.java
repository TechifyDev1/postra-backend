package com.qudus.postra.dtos;

import java.util.Date;

public record PostDto1(
                Long id,
                String title,
                String subTitle,
                String content,
                String postBanner,
                String username,
                String profilePic,
                String slug, String authorFullName, long likeCount, long commentCount, Date createdAt) {
}
