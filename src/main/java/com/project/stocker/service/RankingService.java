package com.project.stocker.service;

import com.project.stocker.dto.response.RankingDecreaseDto;
import com.project.stocker.dto.response.RankingIncreaseDto;
import com.project.stocker.dto.response.RankingVolumeDto;
import com.project.stocker.entity.Buy;
import com.project.stocker.entity.Sell;
import com.project.stocker.entity.Stock;
import com.project.stocker.repository.BuyRepository;
import com.project.stocker.repository.SellRepository;
import com.project.stocker.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class RankingService {

    private final BuyRepository buyRepository;
    private final SellRepository sellRepository;
    private final StockRepository stockRepository;

    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate today = LocalDate.now();

    // 거래량 산출
    @Cacheable(value = "tradeVolume", key = "#stock.company")
    public Long getTradeVolumeForStock(Stock stock) {

//        Stock cachedStock = getCacheForStock(stock);

        Long buyVolumeForStock = buyRepository.findByStock_Company(stock.getCompany())
                .orElse(Collections.emptyList()) // 데이터가 없으면 빈 리스트 반환
                .stream()
                .mapToLong(Buy::getQuantity)
                .sum();

        Long sellVolumeForStock = sellRepository.findByStock_Company(stock.getCompany())
                .orElse(Collections.emptyList())
                .stream()
                .mapToLong(Sell::getQuantity)
                .sum();

        return (buyVolumeForStock + sellVolumeForStock);
    }

    // 상승율 산출
    @Cacheable(value = "tradeIncrease", key = "#stock.company")
    public Double getIncreasePercentage(Stock stock) {
//        Stock cachedStock = getCacheForStock(stock);

        Buy lastBuyOfYesterday = buyRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());
        Buy lastBuyOfToday = buyRepository
                .findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(stock, today.plusDays(1)
                        .atStartOfDay());

        Sell lastSellOfYesterday = sellRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());
        Sell lastSellOfToday = sellRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());

        List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

        if (lastBuyOfYesterday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastBuyOfYesterday.getCreatedAt(), lastBuyOfYesterday.getPrice()));
        }
        if (lastBuyOfToday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastBuyOfToday.getCreatedAt(), lastBuyOfToday.getPrice()));
        }
        if (lastSellOfYesterday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastSellOfYesterday.getCreatedAt(), lastSellOfYesterday.getPrice()));
        }
        if (lastSellOfToday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastSellOfToday.getCreatedAt(), lastSellOfToday.getPrice()));
        }

        sumInDeRecords.sort(Comparator.comparing(SumInDeRecord::getCreatedAt));

        // 가장 빠른 거래와 가장 늦은 거래 선택
        if (sumInDeRecords.size() < 2) {
            return 0.0;
        }
        double firstPriceOfYesterday = sumInDeRecords.get(0).getPrice();
        double lastPriceOfYesterday = sumInDeRecords.get(sumInDeRecords.size() - 1).getPrice();

        if (firstPriceOfYesterday == 0) {
            return 0.0;
        }

        return (lastPriceOfYesterday - firstPriceOfYesterday) / firstPriceOfYesterday * 100;
    }

    //하락율 산출
    @Cacheable(value = "tradeDecrease", key = "#stock.company")
    public Double getDecreasePercentage(Stock stock) {
        // 어제 날짜 가져오기

        Buy lastBuyOfYesterday = buyRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());
        Buy lastBuyOfToday = buyRepository
                .findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(stock, today.plusDays(1)
                        .atStartOfDay());

        Sell lastSellOfYesterday = sellRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());
        Sell lastSellOfToday = sellRepository
                .findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, yesterday.plusDays(1)
                        .atStartOfDay());

        List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

        if (lastBuyOfYesterday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastBuyOfYesterday.getCreatedAt(), lastBuyOfYesterday.getPrice()));
        }
        if (lastBuyOfToday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastBuyOfToday.getCreatedAt(), lastBuyOfToday.getPrice()));
        }
        if (lastSellOfYesterday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastSellOfYesterday.getCreatedAt(), lastSellOfYesterday.getPrice()));
        }
        if (lastSellOfToday != null) {
            sumInDeRecords.add(new SumInDeRecord(lastSellOfToday.getCreatedAt(), lastSellOfToday.getPrice()));
        }


        sumInDeRecords.sort(Comparator.comparing(SumInDeRecord::getCreatedAt));

        // 가장 빠른 거래와 가장 늦은 거래 선택
        if (sumInDeRecords.size() < 2) {
            return 0.0;
        }
        double firstPriceOfYesterday = sumInDeRecords.get(0).getPrice();
        double lastPriceOfYesterday = sumInDeRecords.get(sumInDeRecords.size() - 1).getPrice();


        if (firstPriceOfYesterday == 0) {
            return 0.0;
        }

        return (firstPriceOfYesterday - lastPriceOfYesterday) / firstPriceOfYesterday * 100;
    }

    //거래량 top 10
    @Cacheable(value = "top10ByTradeVolume")
    public List<RankingVolumeDto> getTop10ByTradeVolume() {
        return stockRepository.findAll().stream()
                .distinct()
                .map(stock -> new RankingVolumeDto(stock.getCompany(), getTradeVolumeForStock(stock)))
                .sorted(Comparator.comparing(RankingVolumeDto::getTradeVolume).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //상승율 top 10
    @Cacheable(value = "top10ByTradeIncrease")
    public List<RankingIncreaseDto> getTop10ByIncreasePercentage() {
        List<RankingIncreaseDto> result = new ArrayList<>();
        for (Stock stock : stockRepository.findAll()) {
            result.add(new RankingIncreaseDto(stock.getCompany(), getIncreasePercentage(stock)));
        }
        return result.stream()
                .sorted(Comparator.comparing(RankingIncreaseDto::getIncreasePercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //하락율 top 10
    @Cacheable(value = "top10ByTradeDecrease")
    public List<RankingDecreaseDto> getTop10ByDecreasePercentage() {
        List<RankingDecreaseDto> result = new ArrayList<>();
        for (Stock stock : stockRepository.findAll()) {
            result.add(new RankingDecreaseDto(stock.getCompany(), getDecreasePercentage(stock)));
        }
        return result.stream()
                .sorted(Comparator.comparing(RankingDecreaseDto::getDecreasePercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //DB저장은 아니지만 buy,sell을 합치기 위하여 제공
    class SumInDeRecord {
        private LocalDateTime createdAt;
        private Long price;

        public SumInDeRecord(LocalDateTime createdAt, Long price) {
            this.createdAt = createdAt;
            this.price = price;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public Long getPrice() {
            return price;
        }
    }
}