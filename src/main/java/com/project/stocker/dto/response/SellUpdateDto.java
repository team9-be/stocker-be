package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellUpdateDto extends TradeDto{
    public SellUpdateDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}