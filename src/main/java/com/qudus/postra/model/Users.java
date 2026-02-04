package com.qudus.postra.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    // Auto-incremented BIGINT id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique email, max 150 characters
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Password field
    @Column(nullable = false)
    private String password;

    // Automatically set creation time
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    // One-to-one relationship with user profile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    // ====== Getters and Setters ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
    public List<Follow> getFollowing() {
        return following;
    }
    public void setFollowing(List<Follow> following) {
        this.following = following;
    }
    public List<Follow> getFollowers() {
        return followers;
    }
    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }
}
