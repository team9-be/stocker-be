package com.project.stocker.util;

import com.project.stocker.entity.Trade;
import com.project.stocker.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class YesterdayPrice {

    private final TradeRepository tradeRepository;
    @Cacheable(value = "yesterdayLastPrice", key = "#stockId")
    public Long getYesterdayLastPrice(Long stockId){
        Trade lastTrade = tradeRepository
                .findTop1ByStock_IdAndAndCreatedAtBeforeAndAndCreatedAtAfterOrderByCreatedAtDesc(
                        stockId,
                        LocalDate.now().minusDays(1).atTime(23,59,59),
                        LocalDate.now().minusDays(1).atStartOfDay()
                );
        return lastTrade.getPrice();
    }

    @Cacheable(value = "yesterdayFirstPrice", key = "#stockId")
    public Long getYesterdayFirstPrice(Long stockId){
        Trade lastTrade = tradeRepository
                .findTop1ByStock_IdAndAndCreatedAtBeforeAndAndCreatedAtAfterOrderByCreatedAtAsc(
                        stockId,
                        LocalDate.now().minusDays(1).atTime(23,59,59),
                        LocalDate.now().minusDays(1).atStartOfDay()
                );
        return lastTrade.getPrice();
    }
}
