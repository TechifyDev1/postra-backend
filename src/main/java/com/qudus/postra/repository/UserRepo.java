package com.qudus.postra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findUserByEmail(String email);

    Optional<Users> findUserByUserProfile_UserName(String emailOrUsername);
    
}
