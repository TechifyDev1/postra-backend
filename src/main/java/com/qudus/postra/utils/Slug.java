package com.qudus.postra.utils;

import java.util.UUID;

public class Slug {
    private String title;

    public Slug(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String generateUniqueSlug() {
        String baseSlug = this.title.toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        String UuidSuffix = UUID.randomUUID().toString().substring(0, 6);
        return baseSlug + "-" + UuidSuffix;
    }

}
