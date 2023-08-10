package com.project.stocker.dto.request;

import lombok.Getter;

@Getter
public class TradeDto {
    private long quantity;
    private long price;
    private String status;
}
