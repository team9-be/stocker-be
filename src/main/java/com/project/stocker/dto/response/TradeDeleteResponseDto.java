package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeDeleteResponseDto extends TradeDto{
    public TradeDeleteResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
