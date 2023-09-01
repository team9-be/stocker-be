package com.project.stocker.dto.response;

import com.project.stocker.entity.Account;
import lombok.Getter;

@Getter
public class AccountResponseDto {
    private String stockCompany;
    private Long quantity;
    private Long price;
    private Long totalAssets;



    public AccountResponseDto(Account account) {
        this.totalAssets = account.getTotalAsset();
        this.stockCompany = account.getStockCompany();
        this.quantity = account.getQuantity();
        this.price = account.getPrice();
    }
}
