package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellCreateResponseDto extends TradeDto{
    public SellCreateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
