package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class StockResponseDto {
    private Long stockId;
    private String company;
    private Long price;
    private double change;

    public StockResponseDto(Long stockId, String company, Long price, Long yesterdayLastPrice) {
        this.stockId = stockId;
        this.company = company;
        this.price = price;
        this.change = (double) price / yesterdayLastPrice;
    }
}
