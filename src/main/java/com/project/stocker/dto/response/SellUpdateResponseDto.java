package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellUpdateResponseDto extends TradeDto{
    public SellUpdateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
