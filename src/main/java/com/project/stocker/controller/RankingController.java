package com.project.stocker.controller;

import com.project.stocker.dto.response.RankingDecreaseDto;
import com.project.stocker.dto.response.RankingIncreaseDto;
import com.project.stocker.dto.response.RankingVolumeDto;
import com.project.stocker.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/stocks/volume")
    public ResponseEntity<List<RankingVolumeDto>> getTop10ByTradeVolume() {
        List<RankingVolumeDto> result = rankingService.getTop10ByTradeVolume();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stocks/increase")
    public ResponseEntity<List<RankingIncreaseDto>> getTop10ByIncreaseAmount() {
        List<RankingIncreaseDto> result = rankingService.getTop10ByIncreasePercentage();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stocks/decrease")
    public ResponseEntity<List<RankingDecreaseDto>> getTop10ByDecreaseAmount() {
        List<RankingDecreaseDto> result = rankingService.getTop10ByDecreasePercentage();
        return ResponseEntity.ok(result);
    }


}