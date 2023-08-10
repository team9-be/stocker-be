package com.project.stocker.ranking.dto;

import lombok.Getter;

@Getter
public class RankingVolumeDto {
    private String company;
    private Long tradeVolume;

    public RankingVolumeDto(String company, Long tradeVolume) {
        this.company=company;
        this.tradeVolume=tradeVolume;
    }

}
