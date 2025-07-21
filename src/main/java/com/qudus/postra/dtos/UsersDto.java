package com.qudus.postra.dtos;

public class UsersDto {
    private String username;
    private String fullName;
    private int numOfFollowers;
    private int numOfFollowing;
    private String profilePictureUrl;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }
    public void setNumOfFollowers(int numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }
    public int getNumOfFollowing() {
        return numOfFollowing;
    }
    public void setNumOfFollowing(int numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    
}
