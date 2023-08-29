package com.project.stocker.controller;

import com.project.stocker.dto.request.ConfirmTradeRequestDto;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.ConfirmTradeResponseDto;
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
    public TradeCreateResponseDto buyCreate(@RequestBody TradeCreateRequestDto buyCreateDto) {
        return TradeService.buyOrders(buyCreateDto);
    }

    //BUY CONFIRM
    @PostMapping("/buy/confirm")
    public ConfirmTradeResponseDto buyCreate(@RequestBody ConfirmTradeRequestDto buyCreateDto) {
        return TradeService.buyConfirm(buyCreateDto);
    }

    //BUY UPDATE
    @PutMapping("/buy")
    public TradeUpdateResponseDto buyUpdate(@RequestBody TradeUpdateRequestDto buyUpdateDto) {
        return TradeService.buyUpdate(buyUpdateDto);
    }

    //BUY DELETE
    @DeleteMapping("/buy")
    public TradeDeleteResponseDto buyDelete(@RequestBody TradeDeleteRequestDto buyDeleteDto) {
        return TradeService.buyDelete(buyDeleteDto);
    }

    // SELL
    @PostMapping("/sell")
    public TradeCreateResponseDto sellCreate(@RequestBody TradeCreateRequestDto sellOrdersCreateDto) {
        return TradeService.sellOrders(sellOrdersCreateDto);
    }

    //SELL CONFIRM
    @PostMapping("/sell/confirm")
    public ConfirmTradeResponseDto confirmSell(@RequestBody ConfirmTradeRequestDto confirmTradeRequestDto) {
        return TradeService.sellConfirmCreate(confirmTradeRequestDto);
    }

    //SELL UPDATE
    @PutMapping("/sell")
    public TradeUpdateResponseDto sellUpdate(@RequestBody TradeUpdateRequestDto sellUpdateDto) {

        return TradeService.sellUpdate(sellUpdateDto);
    }

    //SELL DELETE
    @DeleteMapping("/sell")
    public TradeDeleteResponseDto sellDelete(@RequestBody TradeDeleteRequestDto sellDeleteDto) {

        return TradeService.sellDelete(sellDeleteDto);
    }


}
