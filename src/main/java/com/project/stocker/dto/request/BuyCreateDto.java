package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyCreateDto {
    private String stock;
    private Long quantity;
    private Long buy_price;

}
