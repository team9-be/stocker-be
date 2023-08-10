package com.project.stocker.controller;

import com.project.stocker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CrawlingController {

    private final StockService stockService;

    @GetMapping("/api/stock/crawling")
    public void getStock() {
        stockService.saveStockList();
    }

    @GetMapping("/api/stock/crawling/save")
    public void getStockTrade() {
        stockService.saveTradeList();
    }
}
