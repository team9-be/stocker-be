package com.project.stocker.dto.request;

import lombok.Getter;

@Getter
public class AccountAddStockRequest {
    private String stockCompany;
    private Long quantity;
    private Long price;
}
