package com.project.stocker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeCreateRequestDto {
    private String stock;
    private Long quantity;
    private Long price;

}
