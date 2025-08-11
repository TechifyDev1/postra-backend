package com.qudus.postra.dtos;

public class CurrentUserDto extends UsersDto {
    private String email;
    private boolean isCurrentUser;

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
