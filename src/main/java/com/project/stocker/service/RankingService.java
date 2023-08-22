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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private IncreasePercentageCalculator increasePercentageCalculator;
    private DecreasePercentageCalculator decreasePercentageCalculator;

    @PostConstruct
    public void init() {
        this.increasePercentageCalculator = new IncreasePercentageCalculator(buyRepository, sellRepository);
        this.decreasePercentageCalculator = new DecreasePercentageCalculator(buyRepository, sellRepository);
    }


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
    @Cacheable(value = "increasePercent", key = "#stock.company")

    public class IncreasePercentageCalculator extends TradePercentageCalculator{

        public IncreasePercentageCalculator(BuyRepository buyRepository, SellRepository sellRepository) {
            super(buyRepository, sellRepository);
        }

        @Override
        protected Double calculatePercentage(double firstPrice, double lastPrice) {
            return (lastPrice - firstPrice) / firstPrice * 100;
        }
    }

    //하락율 산출
    @Cacheable(value = "DecreasePercent", key = "#stock.company")
    public class DecreasePercentageCalculator extends TradePercentageCalculator{

        public DecreasePercentageCalculator(BuyRepository buyRepository, SellRepository sellRepository) {
            super(buyRepository, sellRepository);
        }

        @Override
        protected Double calculatePercentage(double firstPrice, double lastPrice) {
            return (firstPrice - lastPrice) / firstPrice * 100;
        }
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
            result.add(new RankingIncreaseDto(stock.getCompany(), increasePercentageCalculator.getPercentage(stock)));
        }

        return stockRepository.findAll().stream()
                .map(stock -> new RankingIncreaseDto(stock.getCompany(), increasePercentageCalculator.getPercentage(stock)))
                .sorted(Comparator.comparing(RankingIncreaseDto::getIncreasePercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    //하락율 top 10
    @Cacheable(value = "top10ByTradeDecrease")
    public List<RankingDecreaseDto> getTop10ByDecreasePercentage() {
        return stockRepository.findAll().stream()
                .map(stock -> new RankingDecreaseDto(stock.getCompany(), decreasePercentageCalculator.getPercentage(stock)))
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

    public abstract class TradePercentageCalculator {

        protected BuyRepository buyRepository;
        protected SellRepository sellRepository;

        public TradePercentageCalculator(BuyRepository buyRepository, SellRepository sellRepository) {
            this.buyRepository = buyRepository;
            this.sellRepository = sellRepository;
        }

        protected abstract Double calculatePercentage(double firstPrice, double lastPrice);

        public Double getPercentage(Stock stock) {
            // 오늘의 첫번째 Buy 거래와 마지막 Buy 거래 가져오기
            Buy firstBuyOfToday = buyRepository.findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, today
                    .atStartOfDay());
            Buy lastBuyOfToday = buyRepository.findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(stock, today
                    .plusDays(1).atStartOfDay());   //이 시간 전 최근 데이터 가져옴

            // 오늘의 첫번째 Sell 거래와 마지막 Sell 거래 가져오기
            Sell firstSellOfToday = sellRepository.findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, today
                    .atStartOfDay());
            Sell lastSellOfToday = sellRepository.findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(stock, today
                    .plusDays(1).atStartOfDay());

            List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

            if (firstBuyOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(firstBuyOfToday.getCreatedAt(), firstBuyOfToday.getPrice()));
            }
            if (lastBuyOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(lastBuyOfToday.getCreatedAt(), lastBuyOfToday.getPrice()));
            }
            if (firstSellOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(firstSellOfToday.getCreatedAt(), firstSellOfToday.getPrice()));
            }
            if (lastSellOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(lastSellOfToday.getCreatedAt(), lastSellOfToday.getPrice()));
            }

            sumInDeRecords.sort(Comparator.comparing(SumInDeRecord::getCreatedAt));

            if (sumInDeRecords.size() < 2) {
                return 0.0;
            }

            double firstPrice = sumInDeRecords.get(0).getPrice();
            double lastPrice = sumInDeRecords.get(sumInDeRecords.size() - 1).getPrice();

            if (firstPrice == 0) {
                return 0.0;
            }

            return calculatePercentage(firstPrice, lastPrice);
        }
    }

}