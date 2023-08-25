package com.project.stocker.controller;

import com.project.stocker.dto.response.StockResponseDto;
import com.project.stocker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @GetMapping("/crawling")
    public void getStock() {
        stockService.saveStockList();
    }

    @GetMapping("/crawling/save")
    public void getStockTrade() {
        stockService.saveTradeList();
    }

    @GetMapping("")
    public StockResponseDto getStock(@RequestParam Long stockId){
        StockResponseDto stockResponseDto = stockService.getStock(stockId);
        return stockResponseDto;
    }
}
