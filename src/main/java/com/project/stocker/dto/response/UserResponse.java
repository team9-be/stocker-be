package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {
    private String msg;
    public UserResponse(String msg) {
        this.msg = msg;
    }
}
