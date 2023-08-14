package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingDecreaseDto {
    private String company;
    private Double decreasePercentage;

    public RankingDecreaseDto(String company, Double decreasePercentage) {
        this.company = company;
        this.decreasePercentage = decreasePercentage;
    }
}