package com.qudus.postra.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Posts;

public interface PostRepo extends JpaRepository<Posts, UUID> {
    Optional<Posts> findBySlug(String slug);
}
