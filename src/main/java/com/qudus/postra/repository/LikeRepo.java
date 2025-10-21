package com.qudus.postra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Like;
import com.qudus.postra.model.Posts;
import com.qudus.postra.model.Users;

public interface LikeRepo extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(Users user, Posts post);

    void deleteByUserAndPost(Users user, Posts post);

    long countByPost(Posts post);

    long countByPostSlug(String slug);
}
