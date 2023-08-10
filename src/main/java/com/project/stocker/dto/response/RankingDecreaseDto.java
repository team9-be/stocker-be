package com.project.stocker.dto.response;

import lombok.Getter;

@Getter
public class RankingDecreaseDto {
    private String company;
    private Double decreaseAmount;

    public RankingDecreaseDto(String company, Double decreaseAmount) {
        this.company = company;
        this.decreaseAmount = decreaseAmount;
    }
}
