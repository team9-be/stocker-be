package com.project.stocker.service;

import com.project.stocker.dto.response.RankingDecreaseDto;
import com.project.stocker.dto.response.RankingIncreaseDto;
import com.project.stocker.dto.response.RankingVolumeDto;
import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import com.project.stocker.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import com.project.stocker.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RankingService {

    private final TradeRepository tradeRepository;
    private final StockRepository stockRepository;

    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate today = LocalDate.now();

    private IncreasePercentageCalculator increasePercentageCalculator;
    private DecreasePercentageCalculator decreasePercentageCalculator;

    @PostConstruct
    public void init() {
        this.increasePercentageCalculator = new IncreasePercentageCalculator(tradeRepository);
        this.decreasePercentageCalculator = new DecreasePercentageCalculator(tradeRepository);
    }


    // 거래량 산출
    public Long getTradeVolumeForStock(Stock stock) {

        Long buyVolumeForStock = tradeRepository.findByStockCompany(stock.getCompany())
                .orElse(Collections.emptyList()) // 데이터가 없으면 빈 리스트 반환
                .stream()
                .mapToLong(Trade::getQuantity)
                .sum();

        return buyVolumeForStock;
    }

    // 상승율 계산기
    public class IncreasePercentageCalculator extends TradePercentageCalculator {

        public IncreasePercentageCalculator(TradeRepository tradeRepository) {
            super(tradeRepository);
        }

        @Override
        protected Double calculatePercentage(double firstPrice, double lastPrice) {
            return (lastPrice - firstPrice) / firstPrice * 100;

        }

    }


    // 하락율 계산기
    public class DecreasePercentageCalculator extends TradePercentageCalculator {

        public DecreasePercentageCalculator(TradeRepository tradeRepository) {
            super(tradeRepository);
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

        List<RankingDecreaseDto> result = new ArrayList<>();
        for (Stock stock : stockRepository.findAll()) {
            result.add(new RankingDecreaseDto(stock.getCompany(), decreasePercentageCalculator.getPercentage(stock)));
        }

        return stockRepository.findAll().stream()
                .map(stock -> new RankingDecreaseDto(stock.getCompany(), decreasePercentageCalculator.getPercentage(stock)))
                .sorted(Comparator.comparing(RankingDecreaseDto::getDecreasePercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }


    public abstract class TradePercentageCalculator {

        protected TradeRepository tradeRepository;

        public TradePercentageCalculator(TradeRepository tradeRepository) {
            this.tradeRepository = tradeRepository;
        }

        protected abstract Double calculatePercentage(double firstPrice, double lastPrice);

        // 퍼센트 가져오기
        public Double getPercentage(Stock stock) {
            // 오늘의 첫번째 Buy 거래와 마지막 Buy 거래 가져오기
            Trade firstBuyOfToday = tradeRepository.findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(stock, today
                    .atStartOfDay());
            Trade lastBuyOfToday = tradeRepository.findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(stock, today
                    .plusDays(1).atStartOfDay());   //이 시간 전 최근 데이터 가져옴


            List<SumInDeRecord> sumInDeRecords = new ArrayList<>();

            if (firstBuyOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(firstBuyOfToday.getCreatedAt(), firstBuyOfToday.getPrice()));
            }
            if (lastBuyOfToday != null) {
                sumInDeRecords.add(new SumInDeRecord(lastBuyOfToday.getCreatedAt(), lastBuyOfToday.getPrice()));
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