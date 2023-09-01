package com.project.stocker.repository;

import com.project.stocker.entity.Trade;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Trade> findByStock_IdAndCreatedAtBeforeAndCreatedAtAfter(
            Long stockId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Trade findTop1ByStock_IdAndBuyerIsNotNullAndSellerIsNotNullOrderByCreatedAtDesc(Long stockId);



    Trade findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtDesc(
            Long stockId,
            LocalDateTime yesterdayLast,
            LocalDateTime yesterdayFirst
    );


    Trade findTop1ByStock_IdAndCreatedAtBeforeAndCreatedAtAfterOrderByCreatedAtAsc(
            Long stockId,
            LocalDateTime localDateTime,
            LocalDateTime localDateTime1
    );

}
