package com.project.stocker.repository;

import com.project.stocker.entity.Stock;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Cacheable(value = "stocks", key = "#stockName")
    Optional<Object> findByCompany(String stockName);
}
