package com.project.stocker.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "company")
public class Stock{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String code;


    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "stock")
    private List<Trade> buys = new ArrayList<>();

    @OneToMany(mappedBy = "stock")
    private List<Trade> sells = new ArrayList<>();

    public Stock(String company, String code) {
        this.company = company;
        this.code = code;
        this.status = "ok";
    }
}
