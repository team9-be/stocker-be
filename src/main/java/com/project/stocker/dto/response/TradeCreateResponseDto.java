package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeCreateResponseDto extends TradeDto{
    public TradeCreateResponseDto(int statusCode, String msg) {
        super(statusCode, msg);
    }

}
