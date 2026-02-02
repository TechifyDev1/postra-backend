package com.qudus.postra.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Posts;

public interface PostRepo extends JpaRepository<Posts, Long> {
    Optional<Posts> findBySlug(String slug);

    void deleteBySlug(String slug);

    // Ensure author is fetched together with posts when using pagination to avoid
    // missing/nullable author fields
    @EntityGraph(attributePaths = { "author" })
    Page<Posts> findAll(Pageable pageAble);

    Optional<Posts> findByAuthorUserNameAndSlug(String username, String slug);

    Page<Posts> findByAuthorUserName(String username, Pageable pageable);

}
