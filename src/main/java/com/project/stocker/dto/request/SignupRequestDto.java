package com.project.stocker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    @NotBlank
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}
