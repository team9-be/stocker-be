package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyCreateRequestDto {
    private String stock;
    private Long quantity;
    private Long buy_price;

}
