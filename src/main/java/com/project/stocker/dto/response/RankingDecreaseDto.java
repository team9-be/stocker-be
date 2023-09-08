package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RankingDecreaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String company;
    private Double decreasePercentage;

    public RankingDecreaseDto(String company, Double decreasePercentage) {
        this.company = company;
        this.decreasePercentage = decreasePercentage;
    }
}