package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeUpdateRequestDto {
    private Long trade_id;
    private Long quantity;
    private Long price;
}
