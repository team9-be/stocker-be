package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class StockDto {
    private String company;
    private String code;

    public StockDto(String company, String code) {
        this.company = company;
        this.code = code;
    }
}
