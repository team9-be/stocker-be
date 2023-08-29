package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmTradeResponseDto extends TradeDto{
    public ConfirmTradeResponseDto(int statusCode, String msg){
        super(statusCode, msg);
    }
}
