package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginResponse {
    private String msg;
    public LoginResponse(String msg) {
        this.msg = msg;
    }
}
