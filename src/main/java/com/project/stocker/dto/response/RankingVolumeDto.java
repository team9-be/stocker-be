package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingVolumeDto {
    private String company;
    private Long tradeVolume;

    public RankingVolumeDto(String company, Long tradeVolume) {
        this.company = company;
        this.tradeVolume = tradeVolume;
    }

}