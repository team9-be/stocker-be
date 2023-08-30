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
    public ResponseEntity<String> getStock() {
        stockService.saveStockList();
        return ResponseEntity.ok("주식 종목 정보 크롤링이 완료 되었습니다.");
    }

    @GetMapping("/crawling/save")
    public ResponseEntity<String> getStockTrade() {
        stockService.saveTradeList();
        return ResponseEntity.ok("주식 거래 정보 크롤링이 완료 되었습니다.");
    }

    @GetMapping("")
    public ResponseEntity<StockResponseDto> getStock(@RequestParam Long stockId) {
        return ResponseEntity.ok(stockService.getStock(stockId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> noTradeYesterday(Exception e) {
        return ResponseEntity.ok(e.getMessage());
    }
}
