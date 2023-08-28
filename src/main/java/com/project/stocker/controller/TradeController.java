package com.project.stocker.controller;

import com.project.stocker.dto.request.*;
import com.project.stocker.dto.response.ConfirmSellResponseDto;
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

    //BUY
    @PostMapping("/buy")
    public TradeCreateResponseDto buyCreate(@RequestBody TradeCreateRequestDto buyCreateDto){
        return TradeService.buyCreate(buyCreateDto);
    }


    //기존 메서드와 속도 test용
    @PostMapping("/buy1")
    public TradeCreateResponseDto buyCreate1(@RequestBody TradeCreateRequestDto buyCreateDto){
        return TradeService.sellCreate1(buyCreateDto);
    }

    @PutMapping("/buy")
    public TradeUpdateResponseDto buyUpdate(@RequestBody TradeUpdateRequestDto buyUpdateDto){
        return TradeService.sellUpdate(buyUpdateDto);
    }

    @DeleteMapping("/buy")
    public TradeDeleteResponseDto buyDelete(@RequestBody TradeDeleteRequestDto buyDeleteDto){
        return TradeService.sellDelete(buyDeleteDto);
    }

    // SELL
    @PostMapping("/sell")
    public TradeCreateResponseDto sellCreate(@RequestBody TradeCreateRequestDto sellCreateDto){
        return TradeService.sellCreate(sellCreateDto);
    }

    @PostMapping("/sell/confirm")
    public ConfirmSellResponseDto confirmSell(@RequestBody ConfirmSellRequestDto confirmSellRequestDto){
        return TradeService.confirmSell(confirmSellRequestDto);
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
