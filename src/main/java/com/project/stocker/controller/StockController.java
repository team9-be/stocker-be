package com.project.stocker.controller;

import com.project.stocker.dto.response.StockResponseDto;
import com.project.stocker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
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
    public String getStock(@RequestParam Long stockId, Model model) {
        StockResponseDto stockResponseDto = stockService.getStock(stockId);
        model.addAttribute("stockId", stockResponseDto.getStockId());
        model.addAttribute("company", stockResponseDto.getCompany());
        model.addAttribute("price", stockResponseDto.getPrice());
        model.addAttribute("change", stockResponseDto.getChange());
        return "findStock";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> noTradeYesterday(Exception e) {
        return ResponseEntity.ok(e.getMessage());
    }
}
