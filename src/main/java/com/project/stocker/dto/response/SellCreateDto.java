package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellCreateDto extends TradeDto{
    public SellCreateDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
