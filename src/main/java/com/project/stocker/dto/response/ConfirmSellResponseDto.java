package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmSellResponseDto extends TradeDto{
    public ConfirmSellResponseDto(int statusCode, String msg){
        super(statusCode, msg);
    }
}
