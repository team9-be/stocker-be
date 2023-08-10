package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyDeleteDto extends TradeDto{
    public BuyDeleteDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
