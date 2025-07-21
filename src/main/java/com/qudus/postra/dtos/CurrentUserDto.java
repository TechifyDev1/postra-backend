package com.qudus.postra.dtos;

public class CurrentUserDto extends UsersDto {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
