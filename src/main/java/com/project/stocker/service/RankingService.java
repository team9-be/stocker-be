package com.project.stocker.service;

import com.project.stocker.dto.response.RankingDecreaseDto;
import com.project.stocker.dto.response.RankingIncreaseDto;
import com.project.stocker.dto.response.RankingVolumeDto;
import com.project.stocker.entity.Trade;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.util.YesterdayPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RankingService {

    private final TradeRepository tradeRepository;
    private final StockRepository stockRepository;
    private final YesterdayPrice yesterdayPrice;

    // 거래량 산출

    public Long getTradeVolumeForStock(Long stockId) {
        return tradeRepository.findByStock_IdAndCreatedAtBeforeAndCreatedAtAfter(
                        stockId,
                        LocalDate.now().minusDays(1).atTime(23, 59, 59),
                        LocalDate.now().minusDays(1).atStartOfDay())
                .stream()
                .mapToLong(Trade::getQuantity)
                .sum();
    }

    public Double getPriceRatio(Long stockId) {
        Long yesterdayLastPrice = yesterdayPrice.getYesterdayLastPrice(stockId);
        Long yesterdayFirstPrice = yesterdayPrice.getYesterdayFirstPrice(stockId);
        if (yesterdayLastPrice == null || yesterdayFirstPrice == null) {
            return 0.0;
        }
        return (double) (yesterdayLastPrice - yesterdayFirstPrice) / yesterdayFirstPrice * 100;
    }

    //거래량 top 10
    @Scheduled(cron = "1 0 0 * * *")
    @Cacheable(value = "top10ByTradeVolume")
    public List<RankingVolumeDto> getTop10ByTradeVolume() {
        return stockRepository.findAll().stream()
                .distinct()
                .map(stock -> new RankingVolumeDto(stock.getCompany(), getTradeVolumeForStock(stock.getId())))
                .sorted(Comparator.comparing(RankingVolumeDto::getTradeVolume).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //상승율 top 10
    @Scheduled(cron = "1 0 0 * * *")
    @Cacheable(value = "top10ByTradeIncrease")
    public List<RankingIncreaseDto> getTop10ByIncreasePercentage() {
        return stockRepository.findAll().stream()
                .map(stock -> new RankingIncreaseDto(stock.getCompany(), getPriceRatio(stock.getId())))
                .sorted(Comparator.comparing(RankingIncreaseDto::getIncreasePercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //하락율 top 10
    @Scheduled(cron = "1 0 0 * * *")
    @Cacheable(value = "top10ByTradeDecrease")
    public List<RankingDecreaseDto> getTop10ByDecreasePercentage() {

        return stockRepository.findAll().stream()
                .map(stock -> new RankingDecreaseDto(stock.getCompany(), getPriceRatio(stock.getId())))
                .sorted(Comparator.comparing(RankingDecreaseDto::getDecreasePercentage))
                .limit(10)
                .collect(Collectors.toList());
    }

}