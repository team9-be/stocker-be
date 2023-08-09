package com.project.stocker.entity;

import com.project.stocker.dto.request.StockDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String status;

    public Stock(StockDto stockDto) {
        this.company = stockDto.getCompany();
        this.code = stockDto.getCode();
        this.status = "ok";
    }
}
