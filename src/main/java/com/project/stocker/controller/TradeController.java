package com.project.stocker.controller;

import com.project.stocker.dto.request.*;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService TradeService;

    // BUY
    @PostMapping("/buy")
    public TradeCreateResponseDto buyCreate(@RequestBody TradeCreateRequestDto buyCreateDto){
        return TradeService.buyCreate(buyCreateDto);
    }
    @PutMapping("/buy")
    public TradeUpdateResponseDto buyUpdate(@RequestBody TradeUpdateRequestDto buyUpdateDto){
        return TradeService.buyUpdate(buyUpdateDto);
    }

    @DeleteMapping("/buy")
    public TradeDeleteResponseDto buyDelete(@RequestBody TradeDeleteRequestDto buyDeleteDto){
        return TradeService.buyDelete(buyDeleteDto);
    }

    // SELL
    @PostMapping("/sell")
    public TradeCreateResponseDto sellCreate(@RequestBody TradeCreateRequestDto sellCreateDto){
        return TradeService.sellCreate(sellCreateDto);
    }
    @PutMapping("/sell")
    public TradeUpdateResponseDto sellUpdate(@RequestBody TradeUpdateRequestDto sellUpdateDto){

        return TradeService.sellUpdate(sellUpdateDto);
    }
    @DeleteMapping("/sell")
    public TradeDeleteResponseDto sellDelete(@RequestBody TradeDeleteRequestDto sellDeleteDto){

        return TradeService.sellDelete(sellDeleteDto);
    }
}
