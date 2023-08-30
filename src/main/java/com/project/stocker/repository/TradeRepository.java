package com.project.stocker.repository;

import com.project.stocker.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findByStock_IdAndCreatedAtBeforeAndCreatedAtAfter(Long stockId, LocalDateTime endTime, LocalDateTime startTime);

    Trade findTop1ByStock_IdAndBuyerIsNotNullAndSellerIsNotNullOrderByCreatedAtDesc(Long stockId);

    Trade findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtDesc(Long stockId, LocalDateTime yesterdayLast, LocalDateTime yesterdayFirst);

    Trade findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtAsc(Long stockId, LocalDateTime localDateTime, LocalDateTime localDateTime1);

}
