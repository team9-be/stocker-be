package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellDeleteResponseDto extends TradeDto {
    public SellDeleteResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
