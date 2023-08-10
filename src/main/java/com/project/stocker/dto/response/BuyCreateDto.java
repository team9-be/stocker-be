package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyCreateDto extends TradeDto{
    public BuyCreateDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
