package com.qudus.postra.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "follows")
public class Follow {

    protected Follow(){}

    public Follow(Users follower, Users following) {
        this.follower = follower;
        this.following = following;
        this.id = new FollowId();
        
    }

    @EmbeddedId
    private FollowId id = new FollowId(); // Composite key using @Embeddable class

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private Users follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
    private Users following;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "followed_at")
    private Date followedAt = new Date();

    // ====== Getters and Setters ======

    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
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
