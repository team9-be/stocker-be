package com.project.stocker.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RankingVolumeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String company;
    private Long tradeVolume;

    public RankingVolumeDto(String company, Long tradeVolume) {
        this.company = company;
        this.tradeVolume = tradeVolume;
    }

}