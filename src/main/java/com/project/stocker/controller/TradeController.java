package com.project.stocker.controller;

import com.project.stocker.dto.request.*;
import com.project.stocker.service.BuyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    // Buy
    @GetMapping("/buy")
    public String buyRead(@RequestBody TradeGetDto tradeGetDto){
        return "buy";
    }
    @PostMapping("/buy")
    public com.project.stocker.dto.response.BuyCreateDto buyCreate(@RequestBody BuyCreateDto buyCreateDto){
        return BuyService.buyCreate(buyCreateDto);
    }

    @PutMapping("/buy")
    public String buyUpdate(@RequestBody BuyUpdateDto buyUpdateDto){
        return "buy";
    }

    @DeleteMapping("/buy")
    public String buyDelete(@RequestBody BuyDeleteDto buyDeleteDto){
        return "buy";
    }

    // Sell
    @GetMapping("/sell")
    public String sellRead(@RequestBody TradeGetDto tradeGetDto){
        return "sell";
    }
    @PostMapping("/sell")
    public String sellCreate(@RequestBody SellCreateDto sellCreateDto){
        return "sell";
    }

    @PutMapping("/sell")
    public String sellUpdate(@RequestBody SellUpdateDto sellUpdateDto){
        return "sell";
    }
    @DeleteMapping("/sell")
    public String sellDelete(@RequestBody SellDeleteDto sellDeleteDto){
        return "sell";
    }
}
