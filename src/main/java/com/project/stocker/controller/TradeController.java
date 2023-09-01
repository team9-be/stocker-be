package com.project.stocker.controller;

import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.filter.UserDetailsImpl;
import com.project.stocker.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService TradeService;

    //BUY
    @PostMapping("/buy")
    public TradeCreateResponseDto buyCreate(@RequestBody TradeCreateRequestDto buyCreateDto, HttpServletRequest request) {
        return TradeService.buyOrders(buyCreateDto,request);
    }

    //BUY UPDATE
    @PutMapping("/buy")
    public TradeUpdateResponseDto buyUpdate(@RequestBody TradeUpdateRequestDto buyUpdateDto, HttpServletRequest request) {
        return TradeService.buyUpdate(buyUpdateDto, request);
    }

    //BUY DELETE
    @DeleteMapping("/buy")
    public TradeDeleteResponseDto buyDelete(@RequestBody TradeDeleteRequestDto buyDeleteDto, HttpServletRequest request) {
        return TradeService.buyDelete(buyDeleteDto, request);
    }

    // SELL
    @PostMapping("/sell")
    public TradeCreateResponseDto sellCreate(@RequestBody TradeCreateRequestDto sellOrdersCreateDto,
                                             HttpServletRequest request,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return TradeService.sellOrders(sellOrdersCreateDto,request, userId);
    }

    //SELL UPDATE
    @PutMapping("/sell")
    public TradeUpdateResponseDto sellUpdate(@RequestBody TradeUpdateRequestDto sellUpdateDto, HttpServletRequest request) {

        return TradeService.sellUpdate(sellUpdateDto, request);
    }

    //SELL DELETE
    @DeleteMapping("/sell")
    public TradeDeleteResponseDto sellDelete(@RequestBody TradeDeleteRequestDto sellDeleteDto, HttpServletRequest request) {
        return TradeService.sellDelete(sellDeleteDto, request);
    }
}
