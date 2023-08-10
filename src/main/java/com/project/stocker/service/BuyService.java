package com.project.stocker.service;

import com.project.stocker.dto.response.BuyCreateDto;
import com.project.stocker.dto.response.BuyDeleteDto;
import com.project.stocker.dto.response.BuyUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BuyService {
//    private final StockService stockService;
//    private final UserService userService;
    private final BuyService buyService;

    public static BuyCreateDto buyCreate(com.project.stocker.dto.request.BuyCreateDto buyCreateDto){

        String stockCode = buyCreateDto.getStock();
        Long quantity = buyCreateDto.getQuantity();
        Long buyPrice = buyCreateDto.getBuy_price();

        // stock validation

        // quantity & buyPrice validation -> wallet checking

        return new BuyCreateDto(HttpStatus.OK.value(), "매수 신청 성공.");
    }

    public BuyUpdateDto buyUpdateDto(com.project.stocker.dto.request.BuyUpdateDto buyUpdateDto){

        Long tradeId = buyUpdateDto.getTrade_id();
        Long quantity = buyUpdateDto.getQuantity();
        Long buyPrice = buyUpdateDto.getBuy_price();

        // tradeId validation

        // trade status validation

        // quantity & buyPrice validation -> wallet checking

        return new BuyUpdateDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
    }

    public BuyDeleteDto buyDelete(com.project.stocker.dto.request.BuyDeleteDto buyDeleteDto){
        
        Long tradeId = buyDeleteDto.getTrade_id();

        // trade Id validation

        //trade status validation

        return new BuyDeleteDto(HttpStatus.OK.value(), "매수 취소 성공.");
    }


}
