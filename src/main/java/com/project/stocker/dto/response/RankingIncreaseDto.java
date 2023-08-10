package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class RankingIncreaseDto {
    private String company;
    private Double increaseAmount;

    public RankingIncreaseDto(String company, Double increaseAmount) {
        this.company = company;
        this.increaseAmount = increaseAmount;
    }
}
