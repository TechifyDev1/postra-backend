package com.qudus.postra.dtos;

import java.util.Date;

public class PostDto {
    private String title;
    private String subTitle;
    private String authorUsername;
    private String postBanner;
    private String authorFullName;
    private Long id;
    private String slug;
    private String content;
    private String authorProfilePic;
    private Date createdAt;

    public String getAuthorProfilePic() {
        return authorProfilePic;
    }

    public void setAuthorProfilePic(String authorProfilePic) {
        this.authorProfilePic = authorProfilePic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public PostDto(String title, String subTitle, String postBanner, String authorFullName, Long id,
            String content, String slug, String authorUsername, String authorProfilePic, Date createdAt) {
        this.title = title;
        this.authorUsername = authorUsername;
        this.postBanner = postBanner;
        this.authorFullName = authorFullName;
        this.id = id;
        this.content = content;
        this.slug = slug;
        this.subTitle = subTitle;
        this.authorProfilePic = authorProfilePic;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostDto [title=" + title + ", subTitle=" + subTitle + ", authorUsername=" + authorUsername
                + ", postBanner=" + postBanner + ", authorFullName=" + authorFullName + ", id=" + id + ", slug=" + slug
                + ", content=" + content + "]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getPostBanner() {
        return postBanner;
    }

    public void setPostBanner(String postBanner) {
        this.postBanner = postBanner;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

}
