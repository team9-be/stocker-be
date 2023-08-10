package com.project.stocker.service;

import com.project.stocker.dto.response.SellCreateDto;
import com.project.stocker.dto.response.SellDeleteDto;
import com.project.stocker.dto.response.SellUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SellService {
//    private final StockService stockService;
//    private final UserService userService;
    private final SellService sellService;


    public SellCreateDto sellCreate(com.project.stocker.dto.request.SellCreateDto sellCreateDto) {
        String stock = sellCreateDto.getStock();
        Long quantity = sellCreateDto.getQuantity();
        Long sell_price = sellCreateDto.getSell_price();

        // stock validation

        // quantity validation -> wallet checker

        return new SellCreateDto(HttpStatus.OK.value(), "매도 신청 성공.");
    }

    public SellUpdateDto sellUpdate(com.project.stocker.dto.request.SellUpdateDto sellUpdateDto){
        Long trade_id = sellUpdateDto.getTrade_id();
        Long quantity = sellUpdateDto.getQuantity();
        Long sell_price = sellUpdateDto.getSell_price();

        // trade_id validation

        // quantity and sell_price validation

        return new SellUpdateDto(HttpStatus.OK.value(), "매도 신청 수정 성공.");
    }

    public SellDeleteDto sellDelete(com.project.stocker.dto.request.SellDeleteDto sellDeleteDto){
        Long trade_id = sellDeleteDto.getTrade_id();

        // trade_id validation
        return new SellDeleteDto(HttpStatus.OK.value(), "매도 취소 성공.");
    }

}

