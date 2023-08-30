package com.project.stocker.repository;

import com.project.stocker.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<List<Trade>> findByStockCompany(String company);
    Trade findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(Stock stock, LocalDateTime localDateTime);
    Trade findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(Stock stock, LocalDateTime localDateTime);

}
