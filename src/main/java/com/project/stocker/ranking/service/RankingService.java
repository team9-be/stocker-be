package com.project.stocker.ranking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {
//
//    private final BuyRepository buyRepository;
//    private final SellRepository sellRepository;
//    private final StockRepository stockRepository;
//
////    public Long getTradeVolumeForStock(Stock stock) {
////        Long buyVolumeForStock = buyRepository.findAllByCompany(stock.getCompany()).stream()
////                .mapToLong(Buy::getQuantity)
////                .sum();
////
////        Long sellVolumeForStock = sellRepository.findAllByCompany(stock.getCompany()).stream()
////                .mapToLong(Sell::getQuantity)
////                .sum();
////
////        return buyVolumeForStock + sellVolumeForStock;
////    }
//
//    public Double getIncreaseAmount() {
//        List<Buy> buys = buyRepository.findAll();
//
//        if (buys.size() < 2) return 0.0;
//
//        Double latestPrice = buys.get(0).getPrice();
//        Double previousPrice = buys.get(1).getPrice();
//
//        return latestPrice - previousPrice;
//    }
//
//    public Double getDecreaseAmount() {
//        List<Sell> sells = sellRepository.findAll();
//
//        if (sells.size() < 2) return 0.0;
//
//        Double latestPrice = sells.get(0).getPrice();
//        Double previousPrice = sells.get(1).getPrice();
//
//        return previousPrice - latestPrice;
//    }
//
//    public List<RankingVolumeDto> getTop10ByTradeVolume() {
//
//        return stockRepository.findAll().stream()
//                .map(stock -> new RankingVolumeDto(stock.getCompany(), getTradeVolume()))  // 각 주식의 거래량을 가져옵니다. (이 부분은 실제 구현에 따라 변경이 필요합니다.)
//                .sorted(Comparator.comparing(RankingVolumeDto::getTradeVolume).reversed())
//                .limit(10)
//                .collect(Collectors.toList());
//    }
//
//    public List<RankingIncreaseDto> getTop10ByIncreaseAmount() {
//        // 상승폭 기준 상위 10개 주식을 가져오는 로직
//        List<RankingIncreaseDto> result = new ArrayList<>();
//        for (Stock stock : stockRepository.findAll()) {
//            result.add(new RankingIncreaseDto(stock.getCompany(), getIncreaseAmount()));
//        }
//        return result.stream()
//                .sorted(Comparator.comparing(RankingIncreaseDto::getIncreaseAmount).reversed())
//                .limit(10)
//                .collect(Collectors.toList());
//    }
//
//    public List<RankingDecreaseDto> getTop10ByDecreaseAmount() {
//        List<RankingDecreaseDto> result = new ArrayList<>();
//        for (Stock stock : stockRepository.findAll()) {
//            result.add(new RankingDecreaseDto(stock.getCompany(), getDecreaseAmount()));
//        }
//        return result.stream()
//                .sorted(Comparator.comparing(RankingDecreaseDto::getDecreaseAmount))
//                .limit(10)
//                .collect(Collectors.toList());
//    }
//
//
}