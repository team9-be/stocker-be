package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingIncreaseDto {
    private String company;
    private Double increasePercentage;

    public RankingIncreaseDto(String company, Double increasePercentage) {
        this.company = company;
        this.increasePercentage = increasePercentage;
    }

}