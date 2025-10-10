package com.qudus.postra.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.hibernate.query.Page;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
// import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Posts;

public interface PostRepo extends JpaRepository<Posts, Integer> {
    Optional<Posts> findBySlug(String slug);

    void deleteBySlug(String slug);

    Page<Posts> findAll(Pageable pageAble);

    Optional<Posts> findByAuthorUserNameAndSlug(String username, String slug);
}
