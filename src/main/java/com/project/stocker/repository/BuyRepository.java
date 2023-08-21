package com.project.stocker.repository;

import com.project.stocker.entity.Buy;
import com.project.stocker.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BuyRepository extends JpaRepository<Buy, Long> {
    Optional<List<Buy>> findByStock_Company(String company);


    Buy findFirstByStockAndCreatedAtAfterOrderByCreatedAtAsc(Stock stock, LocalDateTime localDateTime);

    Buy findFirstByStockAndCreatedAtBeforeOrderByCreatedAtDesc(Stock stock, LocalDateTime localDateTime);
}
