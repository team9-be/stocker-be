package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellCreateDto {
    private String stock;
    private Long quantity;
    private Long sell_price;

}
