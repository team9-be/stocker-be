package com.project.stocker.controller;

import com.project.stocker.dto.response.StockResponseDto;
import com.project.stocker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public StockResponseDto getStock(@RequestParam Long stockId) {
        return stockService.getStock(stockId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> noTradeYesterday(Exception e) {
        return ResponseEntity.ok(e.getMessage());
    }
}
