package com.project.stocker.service;

import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TradeService {
    private final TradeRepository tradeRepository;
    private final StockRepository stockRepository;
//    private final UserRepository userRepository;


    // BUY
    public TradeCreateResponseDto buyCreate(TradeCreateRequestDto buyCreateDto){

        String stockName = buyCreateDto.getStock();
        Long quantity = buyCreateDto.getQuantity();
        Long buyPrice = buyCreateDto.getPrice();

        // stock validation
        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(()->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        // quantity & buyPrice validation -> User wallet checking
        Trade trade = new Trade.Builder(quantity, buyPrice, stock)
//                .buyer(user)
                .build();

        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 신청 성공.");
    }

    public TradeUpdateResponseDto buyUpdate(TradeUpdateRequestDto buyUpdateDto){

        Long tradeId = buyUpdateDto.getTrade_id();
        Long quantity = buyUpdateDto.getQuantity();
        Long buyPrice = buyUpdateDto.getPrice();

        // tradeId validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        // trade status validation
        trade.setQuantity(quantity);
        trade.setPrice(buyPrice);
        trade.setStatus("updated");
        tradeRepository.save(trade);
        // quantity & buyPrice validation -> wallet checking

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
    }

    public TradeDeleteResponseDto buyDelete(TradeDeleteRequestDto buyDeleteDto){

        Long tradeId = buyDeleteDto.getTrade_id();

        // trade Id validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        //user validation
        tradeRepository.delete(trade);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매수 취소 성공.");
    }


    // SELL

    public TradeCreateResponseDto sellCreate(TradeCreateRequestDto sellCreateDto) {

        String stockName = sellCreateDto.getStock();
        Long quantity = sellCreateDto.getQuantity();
        Long buyPrice = sellCreateDto.getPrice();

        // stock validation
        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(()->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        // quantity & buyPrice validation -> User wallet checking
        Trade trade = new Trade.Builder(quantity, buyPrice, stock)
//                .seller(user)
                .build();

        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 신청 성공.");
    }

    public TradeUpdateResponseDto sellUpdate(TradeUpdateRequestDto sellUpdateDto){

        Long tradeId = sellUpdateDto.getTrade_id();
        Long quantity = sellUpdateDto.getQuantity();
        Long buyPrice = sellUpdateDto.getPrice();

        // tradeId validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        // trade status validation
        trade.setQuantity(quantity);
        trade.setPrice(buyPrice);
        trade.setStatus("updated");
        tradeRepository.save(trade);
        // quantity & buyPrice validation -> wallet checking

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
    }

    public TradeDeleteResponseDto sellDelete(TradeDeleteRequestDto sellDeleteDto){

        Long tradeId = sellDeleteDto.getTrade_id();

        // trade Id validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));


        // user validation


        tradeRepository.delete(trade);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매수 취소 성공.");
    }



}
