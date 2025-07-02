package com.qudus.postra.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Users;

public interface UserRepo extends JpaRepository<Users, UUID> {

    Optional<Users> findUserByEmail(String email);

    Optional<Users> findUserByUserProfile_UserName(String emailOrUsername);
    
}
