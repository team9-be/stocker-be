package com.project.stocker.repository;

import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<List<Trade>> findByStockCompany(String company);
    List<Trade> findTop2ByStockOrderByCreatedAtDesc(Stock stock);
    Trade findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(Stock stock, LocalDateTime localDateTime);
    Trade findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(Stock stock, LocalDateTime localDateTime);

    List<Trade> findByStockCompanyAndPriceAndQuantityAndBuyerIsNullOrderByCreatedAtAsc(String stockName, Long buyPrice, Long quantity);

}
