package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RankingIncreaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String company;
    private Double increasePercentage;

    public RankingIncreaseDto(String company, Double increasePercentage) {
        this.company = company;
        this.increasePercentage = increasePercentage;
    }

}