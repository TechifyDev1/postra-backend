package com.qudus.postra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.UserProfile;

public interface UserProfileRepo extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findUserByUserName(String userName);

    Optional<UserProfile> findByUser_Email(String email);
}
