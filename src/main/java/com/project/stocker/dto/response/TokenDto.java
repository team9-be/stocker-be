package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class TokenDto {
    private String AccessToken;
    private String RefreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        AccessToken = accessToken;
        RefreshToken = refreshToken;
    }
}
