package com.project.stocker.dto.request;

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
