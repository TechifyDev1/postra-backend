package com.qudus.postra.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "followers")
public class Followers {

    @EmbeddedId
    private FollowerId id = new FollowerId(); // Composite key using @Embeddable class

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private Users follower;

    @ManyToOne
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
    private Users following;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "followed_at")
    private Date followedAt = new Date();

    // ====== Getters and Setters ======

    public FollowerId getId() {
        return id;
    }

    public void setId(FollowerId id) {
        this.id = id;
    }

    public Users getFollower() {
        return follower;
    }

    public void setFollower(Users follower) {
        this.follower = follower;
    }

    public Users getFollowing() {
        return following;
    }

    public void setFollowing(Users following) {
        this.following = following;
    }

    public Date getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Date followedAt) {
        this.followedAt = followedAt;
    }
}
