package com.qudus.postra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Posts;

public interface PostRepo extends JpaRepository<Posts, Integer> {
    Optional<Posts> findBySlug(String slug);
    void deleteBySlug(String slug);
}
