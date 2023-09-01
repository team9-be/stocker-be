package com.project.stocker.entity;

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
    public Account(User user) {
        this.user = user;
    }
    public Account(User user, Trade trade) {
        this.user = user;
        this.stockCompany = trade.getStock().getCompany();
        this.quantity = trade.getQuantity();
        this.price = trade.getPrice();


    }

    public void update(Trade trade) {
        this.stockCompany = trade.getStock().getCompany();
        this.quantity = trade.getQuantity();
        this.price = trade.getPrice();
        this.totalAsset = this.price * this.quantity;
    }
    public void changeQuantity(Long quantity) {
        this.quantity += quantity;
    }
}
