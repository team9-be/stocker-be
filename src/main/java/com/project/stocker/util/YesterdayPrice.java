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
    public Long getYesterdayLastPrice(Long stockId) {
        Trade lastTrade = tradeRepository
                .findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtDesc(
                        stockId,
                        LocalDate.now().minusDays(1).atTime(23, 59, 59),
                        LocalDate.now().minusDays(1).atStartOfDay()
                );
        if (lastTrade == null) {
            return null;
        }
        return lastTrade.getPrice();
    }

    public Long getYesterdayFirstPrice(Long stockId) {
        Trade lastTrade = tradeRepository
                .findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtAsc(
                        stockId,
                        LocalDate.now().minusDays(1).atTime(23, 59, 59),
                        LocalDate.now().minusDays(1).atStartOfDay()
                );
        if (lastTrade == null) {
            return null;
        }
        return lastTrade.getPrice();
    }
}
