package com.project.stocker.entity;

import com.project.stocker.dto.request.AccountAddStockRequest;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockCompany;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Long quantity = 0L;
    private Long price;
    private Long totalAsset = 0L;

    public Account(User user, Trade trade) {
        this.user = user;
        this.stockCompany = trade.getStock().getCompany();
        this.quantity = trade.getQuantity();
        this.price = trade.getPrice();
        this.totalAsset = this.price * this.quantity;

    }

    public Account(User user, TradeCreateRequestDto ordersCreateRequestDto) {
        this.user = user;
        this.stockCompany = ordersCreateRequestDto.getStock();
        this.price = 0L;
        this.quantity = 0L;
        this.totalAsset = 0L;
    }

    public Account(User user, AccountAddStockRequest requestDto) {
        this.user = user;
        this.quantity = requestDto.getQuantity();
        this.stockCompany = requestDto.getStockCompany();
        this.price = requestDto.getPrice();
        this.totalAsset = this.price * this.quantity;
    }

    public void changeQuantity(Long quantity) {
        this.quantity += quantity;
        this.totalAsset = this.price * this.quantity;
    }
}
