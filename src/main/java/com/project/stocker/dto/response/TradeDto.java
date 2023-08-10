package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeDto {
    private int statusCode;
    private String msg;

    public TradeDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
