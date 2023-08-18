package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyUpdateRequestDto {
    private Long trade_id;
    private Long quantity;
    private Long buy_price;
}
