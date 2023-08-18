package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyUpdateResponseDto extends TradeDto{
    public BuyUpdateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
