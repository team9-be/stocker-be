package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class StockResponseDto {
    private final Long stockId;
    private final String company;
    private final Long price;
    private final double change;

    public StockResponseDto(Long stockId, String company, Long price, Long yesterdayLastPrice) {
        if (yesterdayLastPrice == null)
            yesterdayLastPrice = price;
        this.stockId = stockId;
        this.company = company;
        this.price = price;
        this.change = (double) (yesterdayLastPrice - price) / yesterdayLastPrice * 100;
    }
}
