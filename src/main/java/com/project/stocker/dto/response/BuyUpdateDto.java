package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyUpdateDto extends TradeDto{
    public BuyUpdateDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
