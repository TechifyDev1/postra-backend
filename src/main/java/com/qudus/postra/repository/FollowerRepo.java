package com.qudus.postra.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qudus.postra.model.FollowId;
import com.qudus.postra.model.Follow;
import com.qudus.postra.model.Users;


@Repository
public interface FollowerRepo extends JpaRepository<Follow, FollowId> {

    // Gets who this user is follwing
    List<Follow> findByFollower(Users follower);

    // Gets who is following the user
    List<Follow> findByFollowing(Users following);

    // Check if already following
    boolean existsByFollowerAndFollowing(Users follower, Users following);

    // delete follow relationship
    void deleteByFollowerAndFollowing(Users follower, Users following);
}
