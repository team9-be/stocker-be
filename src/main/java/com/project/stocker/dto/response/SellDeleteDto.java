package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellDeleteDto extends TradeDto {
    public SellDeleteDto(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
