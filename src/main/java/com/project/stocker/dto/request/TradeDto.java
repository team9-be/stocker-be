package com.project.stocker.dto.request;

import com.project.stocker.entity.Stock;
import lombok.Getter;

@Getter
public class TradeDto {
    private long quantity;
    private long price;
    private Stock stock;

    public TradeDto(long quantity, long price, Stock stock) {
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
    }
}
