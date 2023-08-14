package com.project.stocker.repository;

import com.project.stocker.entity.Sell;
import com.project.stocker.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellRepository extends JpaRepository<Sell, Long>{
    Optional<List<Sell>> findByStock_Company(String company);
    List<Sell> findTop2ByStockOrderByCreatedAtDesc(Stock stock);
}
