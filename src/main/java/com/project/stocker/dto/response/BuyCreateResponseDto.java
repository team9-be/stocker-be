package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyCreateResponseDto extends TradeDto{
    public BuyCreateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }

}
