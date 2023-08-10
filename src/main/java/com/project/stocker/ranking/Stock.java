package com.project.stocker.ranking;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(nullable = false,length = 5000)
    private String company;
    @Column(nullable = false,length = 5000)
    private String status;
}
