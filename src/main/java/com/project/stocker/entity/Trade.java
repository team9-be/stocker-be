package com.project.stocker.entity;

import com.project.stocker.util.Auditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Trade extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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


    private Trade(Builder builder) {
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.status = "pending";
        this.stock = builder.stock;
        this.buyer = builder.buyer;
        this.seller = builder.seller;
    }


    public static class Builder {
        private Long quantity;
        private Long price;
        private Stock stock;
        private User buyer;
        private User seller;
        private String status;

        public Builder(Long quantity, Long price, Stock stock) {
            this.quantity = quantity;
            this.price = price;
            this.stock = stock;
        }



        public Builder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public Builder seller(User seller) {
            this.seller = seller;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Trade build() {
            return new Trade(this);
        }

    }

    public Trade(long quantity, long price, Stock stock, User user) {
        super();
    }


}
