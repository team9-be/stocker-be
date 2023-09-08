package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeUpdateResponseDto extends TradeDto{
    public TradeUpdateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
