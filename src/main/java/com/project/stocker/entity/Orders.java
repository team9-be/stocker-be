package com.project.stocker.entity;

import com.project.stocker.util.Auditing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orders extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Long quantity;

    @Column
    private Long price;

    @Column
    private String status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = true)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = true)
    private User seller;

    private Orders(Builder builder) {
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.status = "pending";
        this.stock = builder.stock;
        this.buyer = builder.buyer;
        this.seller = builder.seller;
    }


    public static class Builder {
        private final Long quantity;
        private final Long price;
        private final Stock stock;
        private User buyer;
        private User seller;
        private String status;

        public Builder(Long quantity, Long price, Stock stock) {
            this.quantity = quantity;
            this.price = price;
            this.stock = stock;
        }


        public Orders.Builder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public Orders.Builder seller(User seller) {
            this.seller = seller;
            return this;
        }

        public Orders.Builder status(String status) {
            this.status = status;
            return this;
        }

        public Orders build() {
            return new Orders(this);
        }

    }

}
