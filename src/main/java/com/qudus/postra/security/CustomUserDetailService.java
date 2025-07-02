package com.qudus.postra.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qudus.postra.model.Users;
import com.qudus.postra.repository.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Optional<Users> userOpt;
        if (emailOrUsername.contains("@")) {
            userOpt = userRepo.findUserByEmail(emailOrUsername);
        } else {
            userOpt = userRepo.findUserByUserProfile_UserName(emailOrUsername);
        }
        return new UserPrinciple(userOpt.get());
    }
}
