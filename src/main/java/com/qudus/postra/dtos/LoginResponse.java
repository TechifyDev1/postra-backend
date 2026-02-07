package com.qudus.postra.dtos;

public class LoginResponse extends UsersDto {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
