package com.qudus.postra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Comment;
import com.qudus.postra.model.Posts;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByPost(Posts post);

    long countByPostId(Long postId);

}
