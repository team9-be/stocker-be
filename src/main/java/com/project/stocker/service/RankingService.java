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

    // 거래량 산출
    @Cacheable(value = "tradeVolume", key = "#stock.company")
    public Long getTradeVolumeForStock(Stock stock) {
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

        List<Buy> buys = buyRepository.findTop2ByStockOrderByCreatedAtDesc(stock);
        List<Sell> sells = sellRepository.findTop2ByStockOrderByCreatedAtDesc(stock);

        if (sells.size() < 2) return 0.0;
        if (buys.size() < 2) return 0.0;

        List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

        for (Buy buy : buys) {
            sumInDeRecords.add(new SumInDeRecord(buy.getCreatedAt(), buy.getPrice()));
        }
        for (Sell sell : sells) {
            sumInDeRecords.add(new SumInDeRecord(sell.getCreatedAt(), sell.getPrice()));
        }

        sumInDeRecords.sort(Comparator.comparing(SumInDeRecord::getCreatedAt).reversed());


        // 최근의 두 거래를 선택
        double latestPrice = sumInDeRecords.get(0).getPrice();
        double previousPrice = sumInDeRecords.get(1).getPrice();

        if (previousPrice == 0) {
            return 0.0;
        }

        return (latestPrice - previousPrice) / previousPrice * 100;
    }

    //하락율 산출
    @Cacheable(value = "tradeDecrease", key = "#stock.company")
    public Double getDecreasePercentage(Stock stock) {
        List<Buy> buys = buyRepository.findTop2ByStockOrderByCreatedAtDesc(stock);
        List<Sell> sells = sellRepository.findTop2ByStockOrderByCreatedAtDesc(stock);

        if (sells.size() < 2) return 0.0;
        if (buys.size() < 2) return 0.0;

        List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

        for (Buy buy : buys) {
            sumInDeRecords.add(new SumInDeRecord(buy.getCreatedAt(), buy.getPrice()));
        }
        for (Sell sell : sells) {
            sumInDeRecords.add(new SumInDeRecord(sell.getCreatedAt(), sell.getPrice()));
        }


        sumInDeRecords.sort(Comparator.comparing(SumInDeRecord::getCreatedAt).reversed());

        Double latestPrice = Double.valueOf(sumInDeRecords.get(0).getPrice());
        Double previousPrice = Double.valueOf(sumInDeRecords.get(1).getPrice());

        return (previousPrice - latestPrice) / previousPrice * 100;
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
    public List<RankingDecreaseDto> getTop10ByDecreasePercetage() {
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